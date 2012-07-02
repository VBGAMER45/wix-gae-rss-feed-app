package com.wixpress.testapp.controller;

import com.wixpress.testapp.domain.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@Controller
@RequestMapping(SampleApp.EndpointControllerUrl)
public class SampleAppController
{
    @Resource
    private SampleApp sampleApp;

    protected AuthenticationResolver authenticationResolver = new AuthenticationResolver(new ObjectMapper());

    //// Test ////

    @RequestMapping(value = "/widget", method = RequestMethod.GET)
    public String widgetTest(Model model,
                             String instance,
//                             @RequestParam(value = "section-url") String sectionUrl,
//                             @RequestParam String target,
                             Integer width)
    {
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(sampleApp.getApplicationSecret(), instance);
        SampleAppInstance appInstance = sampleApp.getAppInstance(wixSignedInstance);

        if(appInstance == null) //new Instance created
        {
            appInstance = sampleApp.addAppInstance(wixSignedInstance);
        }

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "widget";
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String settingsTest(Model model,
                               String instance,
                               Integer width)
    {
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(sampleApp.getApplicationSecret(), instance);
        SampleAppInstance appInstance = sampleApp.getAppInstance(wixSignedInstance);

        if(appInstance == null) //new Instance created
        {
            appInstance = sampleApp.addAppInstance(wixSignedInstance);
        }

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "settings";
    }

    @RequestMapping(value = "/settingsupdate", method = RequestMethod.GET)
    @ResponseBody
    public Result<Void> widgetUpdate(@RequestParam(required = false) String instanceId,
                                     @RequestParam(required = false) String color,
                                     @RequestParam(required = false) String title)
    {
        SampleAppInstance appInstance = sampleApp.getAppInstance(UUID.fromString(instanceId));

        appInstance.update(color, title);

        return success();
    }

    @RequestMapping(value = "/widgetstandalone", method = RequestMethod.GET)
    public String widgetStandAlone(Model model,
                                   String instanceId,
                                   Integer width)
    {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(instanceId);
        } catch (Exception ignored) {}

        SampleAppInstance appInstance = sampleApp.getAppInstance(uuid);

        if(appInstance == null) //new Instance created
        {
            appInstance = sampleApp.addAppInstance(new WixSignedInstance(uuid, new DateTime(), null, ""));
        }

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "widget";
    }

    @RequestMapping(value = "/settingsstandalone", method = RequestMethod.GET)
    public String settingsStandAlone(Model model,
                                     String instanceId,
                                     Integer width)
    {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(instanceId);
        } catch (Exception ignored) {}

        SampleAppInstance appInstance = sampleApp.getAppInstance(uuid);

        if(appInstance == null) //new Instance created
        {
            appInstance = sampleApp.addAppInstance(new WixSignedInstance(uuid, new DateTime(), null, ""));
        }

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "settings";
    }

    //For testing override only!!
    @RequestMapping(value = "/sampleappupdate", method = RequestMethod.GET)
    @ResponseBody
    public Result<Void> sampleAppUpdate(@RequestParam String applicationID,
                                        @RequestParam String applicationSecret)
    {
        sampleApp.setApplicationID(applicationID);
        sampleApp.setApplicationSecret(applicationSecret);

        return success();
    }


    //// Utils ////

    protected Result<Void> success()
    {
        return new Result<Void>();
    }

}