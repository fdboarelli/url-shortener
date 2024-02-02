/*
    THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
    INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.boarelli.playground.service;

import com.boarelli.playground.components.UrlShortener;
import com.boarelli.playground.model.dtos.UrlDTO;
import com.boarelli.playground.model.entities.UrlEntity;
import com.boarelli.playground.model.errors.UrlAlreadyExistsException;
import com.boarelli.playground.model.errors.UrlNotFoundException;
import com.boarelli.playground.model.errors.UrlNotValidException;
import com.boarelli.playground.model.mapper.UrlMapper;
import com.boarelli.playground.model.web.CreateUrlResponse;
import com.boarelli.playground.repository.UrlDTORepository;
import com.boarelli.playground.repository.UrlEntityRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class UrlShortenerService {

    private final String baseUrl;
    private final UrlShortener urlShortener;
    private final UrlEntityRepository urlEntityRepository;
    private final UrlDTORepository urlDTORepository;
    private final UrlMapper urlMapper;
    private final UrlValidator urlValidator;


    @Autowired
    public UrlShortenerService(@Value("${default.short.base.url}") String baseUrl,
                               UrlShortener urlShortener, UrlMapper urlMapper,
                               UrlEntityRepository urlEntityRepository,
                               UrlDTORepository urlDTORepository) {
        this.baseUrl = baseUrl;
        this.urlShortener = urlShortener;
        this.urlValidator = new UrlValidator();
        this.urlMapper = urlMapper;
        this.urlEntityRepository = urlEntityRepository;
        this.urlDTORepository = urlDTORepository;
    }

    @PostConstruct
    void initCache() {
        var existingUrls = urlMapper.urlEntitiesToDtos(Streamable.of(urlEntityRepository.findAll()).toList());
        log.info("Initializing service cache: found {} existing urls", existingUrls.size());
        urlDTORepository.saveAll(existingUrls);
    }

    public List<UrlDTO> getAllUrls() {
        return urlMapper.urlEntitiesToDtos(Streamable.of(urlEntityRepository.findAll()).toList());
    }

    public UrlDTO getUrl(String urlId) {
        if (StringUtils.isBlank(urlId)) {
            var message = String.format("url %s not valid", urlId);
            log.warn(message);
            throw new IllegalArgumentException(message);
        }

        // Look if url is present in cache
        var cachedUrlDto = urlDTORepository.findById(UUID.fromString(urlId));

        if (cachedUrlDto.isPresent()) {
            return cachedUrlDto.get();
        }

        // Look if url is present in database
        var urlDTO = urlMapper.urlEntityToDto(urlEntityRepository.findById(UUID.fromString(urlId)).orElseThrow(
                () -> {
                    var message = String.format("url %s not found", urlId);
                    log.warn(message);
                    return new UrlNotFoundException(message);
                }));

        urlDTORepository.save(urlDTO);

        return urlDTO;
    }

    public CreateUrlResponse createCompressedUrl(String url) {
        if (!urlValidator.isValid(url)) {
            var message = String.format("url %s is not valid", url);
            log.warn(message, url);
            throw new UrlNotValidException(message);
        }

        var shortUrl = baseUrl.concat(urlShortener.generateHashFromUrl(url));

        if (urlDTORepository.findByShortUrl(shortUrl).isPresent()) {
            var message = String.format("Short url %s already exists in cache for given url %s", shortUrl, url);
            log.warn(message);
            throw new UrlAlreadyExistsException(message);
        }

        if (urlEntityRepository.findByShortUrl(shortUrl).isPresent()) {
            var message = String.format("Short url %s already exists for given url %s", shortUrl, url);
            log.warn(message);
            throw new UrlAlreadyExistsException(message);
        }

        var urlEntity = urlEntityRepository.save(UrlEntity.builder().shortUrl(shortUrl).originalUrl(url).build());
        log.info("New url added in db");

        urlDTORepository.save(urlMapper.urlEntityToDto(urlEntity));
        log.info("New url added in cache");

        return CreateUrlResponse.builder().url(shortUrl).build();
    }

    public void deleteUrl(String urlId) {
        if (StringUtils.isBlank(urlId)) {
            var message = String.format("url %s not valid", urlId);
            log.warn(message);
            throw new IllegalArgumentException(message);
        }

        var deletedMessage = String.format("Deleted url with id %s", urlId);

        try {
            urlDTORepository.deleteById(UUID.fromString(urlId));
            log.debug(deletedMessage + " from cache");
            urlEntityRepository.deleteById(UUID.fromString(urlId));
            log.debug(deletedMessage + " from database");
        } catch (RuntimeException ex) {
            var message = String.format("cannot delete url %s", urlId);
            log.warn(message);
            log.error(ex.getStackTrace());
            throw new UrlNotFoundException(message);
        }
    }

}
