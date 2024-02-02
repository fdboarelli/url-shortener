/*
    THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
    INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.boarelli.playground.resources;

import com.boarelli.playground.model.errors.ErrorMessage;
import com.boarelli.playground.model.errors.UrlAlreadyExistsException;
import com.boarelli.playground.model.errors.UrlNotFoundException;
import com.boarelli.playground.model.errors.UrlNotValidException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UrlNotValidException.class)
    @ResponseBody ErrorMessage handleCreateUrlBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorMessage("url is not valid");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody ErrorMessage handleGenericBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorMessage("request parameters are not valid");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UrlNotFoundException.class)
    @ResponseBody
    ErrorMessage handleNotFoundRequest(HttpServletRequest req, Exception ex) {
        return new ErrorMessage("url not found");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UrlAlreadyExistsException.class)
    @ResponseBody
    ErrorMessage handleConflictRequest(HttpServletRequest req, Exception ex) {
        return new ErrorMessage("url already has compressed version");
    }

}
