package hello.servlet.web.frontcontroller;

import java.util.HashMap;
import java.util.Map;
//스프링 mvc에는 모델엔뷰가 따로잇다.
public class ModelView {
    private String viewName;
    private Map<String, Object> model = new HashMap<>();
    public Map<String, Object> getModel() {
        return model;
    }
    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public ModelView(String viewName) {
        this.viewName = viewName;
    }
}
