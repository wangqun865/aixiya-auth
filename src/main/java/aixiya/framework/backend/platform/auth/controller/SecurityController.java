package aixiya.framework.backend.platform.auth.controller;

import aixiya.framework.backend.platform.auth.service.ValidateCodeService;
import com.aixiya.framework.backend.common.entity.CurrentUser;
import com.aixiya.framework.backend.common.exception.ValidateCodeException;
import com.aixiya.framework.backend.common.utils.AixiyaFwUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * @author wangqun865@163.com
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class SecurityController {

    private final ValidateCodeService validateCodeService;

    @ResponseBody
    @GetMapping("user")
    public Principal currentUser(Principal principal) throws Exception{
        return principal;
    }

    @ResponseBody
    @GetMapping("ttt")
    public CurrentUser currentUser() throws Exception{
        CurrentUser u = AixiyaFwUtil.getCurrentUser();
        return u;

    }



    @ResponseBody
    @GetMapping("test")
    @PreAuthorize("hasAuthority('test:test')")
    public String test() throws Exception{
        return "200";
    }

    @ResponseBody
    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }



}
