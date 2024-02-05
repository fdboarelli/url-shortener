/*
    THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
    INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.boarelli.playground.components;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Component
public class UrlShortener {
    private final MessageDigest digest;

    public UrlShortener() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA3-256");
    }

    public String generateHashFromUrl(String url) {
        var inputWithTimestamp = url.concat(Long.toString(Instant.now().toEpochMilli()));
        return DigestUtils.sha3_256Hex(digest.digest(inputWithTimestamp.getBytes(StandardCharsets.UTF_8))).substring(0, 9);
    }

}
