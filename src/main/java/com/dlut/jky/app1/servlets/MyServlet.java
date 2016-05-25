package com.dlut.jky.app1.servlets;


import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by jiangkunyou on 15/11/1.
 */
public class MyServlet extends HttpServlet {

//    @Autowired
//    private User user;

    private ServletConfig servletConfig;
    public MyServlet() throws JsonProcessingException {
//        System.out.println("construction");
//        System.out.println(user);

//        System.out.println("---------------" + new ObjectMapper().writeValueAsString(new User()));

    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

//        System.out.println("service");
//        user.setUserId(1);
//        System.out.println("----------------" + user);
        // 获取文件输入流的另一种方法 一般用classLoader加载src下的文件
        try {
            ClassLoader cl = getClass().getClassLoader();
            InputStream is = cl.getResourceAsStream("jdbc.properties");
//            System.out.println(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String interesting = req.getParameter("interesting");
//        System.out.println(interesting);

        String [] interestings = req.getParameterValues("interesting");
//        for(String interest: interestings){
//            System.out.println("-->" + interest);
//        }

        Enumeration<String> names = req.getParameterNames();
//        while(names.hasMoreElements()){
//            String name = names.nextElement();
//            String val = req.getParameter(name);
//
//            System.out.println("^^" + name + ": " + val);
//        }

        Map<String, String[]> map = req.getParameterMap();
//        for(Map.Entry<String, String[]> entry: map.entrySet()){
//            System.out.println("**" + entry.getKey() + ":" + Arrays.asList(entry.getValue()));
//        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) req;


        // 获取端口后后面的路径地址比如 /mybigdata/sss.jsp
        String requestURI = httpServletRequest.getRequestURI();
//        System.out.println(requestURI);

        // 获取请求方法名 如POST
        String method = httpServletRequest.getMethod();
//        System.out.println(method);

        // 获取后面的参数比如user=root&password=root
        String queryString = httpServletRequest.getQueryString();
//        System.out.println(queryString);

        // 获取servlet名 如/login
        String servletPath = httpServletRequest.getServletPath();
//        System.out.println(servletPath);


//        res.setContentType("application/msword");

//        PrintWriter out = res.getWriter();
//        out.println("helloworld...");
//        req.getRequestDispatcher("/login.vm").forward(req, res);

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
        String configPara = config.getInitParameter("user");
//        System.out.println("servletConfigPara: " + configPara);

        ServletContext servletContext = config.getServletContext();
        String servletContextPara = servletContext.getInitParameter("driver");
//        System.out.println("servletContextPara: " + servletContextPara);
        // 获取当前的项目名 也就是mybigdata
//        System.out.println("contextPath: " + servletContext.getContextPath());
        // 获取note.txt在网站发布以后，在服务器上的绝对路径，这里的参数是相对于当前web文件夹的相对路径
//        System.out.println("realPath: " + servletContext.getRealPath("/note.txt"));
        // 获取web应用中的某个文件的输入流 注意不能少/
//        System.out.println("***************************fileInputStream: " + servletContext.getResourceAsStream("/WEB-INF/note.txt"));
//        System.out.println("src下的文件inputStream: " + servletContext.getResourceAsStream(""));
        // 获取文件输入流的另一种方法
        try {
            ClassLoader cl = getClass().getClassLoader();
            cl.getResourceAsStream("jdbc.properties");
//            System.out.println("***************************jdbcStream: " + cl.getResourceAsStream("jdbc.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
