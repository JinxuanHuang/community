package life.majiang.community.controller;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

  @Autowired
  private UserMapper userMapper;

  @GetMapping({"/","/index"})
  public String hello(HttpServletRequest request){
    //加载首页时找页面有没有cookies 如果有则把用户名直接放进登陆里面
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if(cookie.getName().equals("token")){
        String token = cookie.getValue();
        User user = userMapper.findBytoken(token);
        if(user!= null){
          request.getSession().setAttribute("user",user);
        }
        break;
      }
    }


    return "index";
  }
}
