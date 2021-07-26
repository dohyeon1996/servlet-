package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.ControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//서블릿이라는걸인지하고 service 메소드를 실행한다.
@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }
    /*컨테이너가 관리하는서블릿 url을클릭하면 컨테이너는 요청된 request가 서블릿이라는걸 간파하고 두개객체를생성한다.
    *HttpServletRequest,HttpServletResponse이거두개를 생성하고 어떤서블릿이요청되엇는지 객체참조를넘긴다
    * 서블릿스레드를 생성하고 service()메소드를호출한다. 그리고 겟방식이나 포스트방식중에 골라서 쓸수있다.  */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
        //리퀘스트에 들어온거임 url
        String requestURI = request.getRequestURI();
        ControllerV3 controller = controllerMap.get(requestURI);
    //url 이널이면 에러뜨고 아니면
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
       // 맵에다가저장한다.
        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);
        String viewName = mv.getViewName();//논리이름 new-form 넣은걸 viewResolver에서 원래 경로로바꿔준다.

        MyView view=viewResolver(viewName);
        //결론은 여기서 html로 뿌려주는거같다.
        view.render(mv.getModel(),request, response);
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