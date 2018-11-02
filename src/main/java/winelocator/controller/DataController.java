package winelocator.controller;

import winelocator.dao.MongoDao;
import winelocator.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataController {
    @Autowired
    DataService dataService;

    @Autowired
    MongoDao mongoDao;

    @RequestMapping("/admin/data")
    public ModelAndView index(){
        ModelAndView mav = new ModelAndView();
        dataService.setData(mongoDao.getData());
        mav.addObject("data", dataService.getData());
        mav.setViewName("data");
        return mav;
    }
}
