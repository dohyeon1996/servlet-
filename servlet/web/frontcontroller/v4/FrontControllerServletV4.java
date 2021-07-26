package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//서블릿이라는걸인지하고 service 메소드를 실행한다.
@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }
    /*컨테이너가 관리하는서블릿 url을클릭하면 컨테이너는 요청된 request가 서블릿이라는걸 간파하고 두개객체를생성한다.
    *HttpServletRequest,HttpServletResponse이거두개를 생성하고 어떤서블릿이요청되엇는지 객체참조를넘긴다
    * 서블릿스레드를 생성하고 service()메소드를호출한다. 그리고 겟방식이나 포스트방식중에 골라서 쓸수있다.  */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //리퀘스트에 들어온거임 url
        String requestURI = request.getRequestURI();
        ControllerV4 controller = controllerMap.get(requestURI);
    //url 이널이면 에러뜨고 아니면
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
       // 맵에다가저장한다.
        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model=new HashMap<>(); //모델부분의 추가

        String viewName = controller.process(paramMap, model);
        MyView view = viewResolver(viewName);

        view.render(model,request,response);

    }
    private MyView viewResolver(String viewName) {
       return  new MyView("WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String,String>  paramMap=new HashMap<>();
        //paramMap 을넘겨줘야한다. 그러려면 다꺼내야함 ....
        request.getParameterNames().asIterator()
                    .forEachRemaining(paramName-> paramMap.put(paramName, request.getParameter(paramName)));

        return paramMap;
    }
}