package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

  @Autowired
  private GithubProvider githubProvider;

  @Value("${github.client.id}")
  private String clientId;

  @Value("${github.client.secret}")
  private String clientSecret;

  @Value("${github.redireect_uri}")
  private String  redirectUri;

  @Autowired
  private UserMapper userMapper;


  @GetMapping("/callback")
  public String callback(@RequestParam(name = "code")String code,
                         @RequestParam(name = "state")String state,
                         HttpServletRequest request){
    AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
    accessTokenDTO.setClient_id(clientId);
    accessTokenDTO.setClient_secret(clientSecret);
    accessTokenDTO.setCode(code);
    accessTokenDTO.setRedireect_uri(redirectUri);
    accessTokenDTO.setState(state);
    String accessToken = githubProvider.getAccessToken(accessTokenDTO);
    GithubUser githubUser = githubProvider.getUser(accessToken);
    System.out.println(githubUser);
    if(githubUser!=null){
      //登陆成功，写Cookies,和session
      //把数据加进数据库
      User user = new User();
      user.setToken(UUID.randomUUID().toString());
      user.setAccountId(String.valueOf(githubUser.getId()));
      user.setName(githubUser.getName());
      user.setGmtCreate(System.currentTimeMillis());
      user.setGmtModified(user.getGmtCreate());
      userMapper.insert(user);

      request.getSession().setAttribute("user",githubUser);
      return "redirect:index"; //重定向重新跳转到index
    }
    else{
      return "redirect:index"; //重定向重新跳转到index
    }
    //return "index";
  }
}
