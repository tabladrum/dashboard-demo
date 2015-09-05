package com.vaadin.demo.dashboard.view;

import com.vaadin.demo.dashboard.view.dashboard.DashboardView;
import com.vaadin.demo.dashboard.view.listening.ListeningView;
import com.vaadin.demo.dashboard.view.livesesson.LiveLessonView;
import com.vaadin.demo.dashboard.view.portfolio.PortfolioView;
import com.vaadin.demo.dashboard.view.practice.PracticeView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum DashboardViewType {
    DASHBOARD("main", DashboardView.class, FontAwesome.HOME, true), LIVELESSON(
            "live lessons", LiveLessonView.class, FontAwesome.BAR_CHART_O, false), PORTFOLIOS(
            "portfolio", PortfolioView.class, FontAwesome.TABLE, false), LISTENING(
            "listening", ListeningView.class, FontAwesome.FILE_TEXT_O, true), PRACTICE(
            "practice", PracticeView.class, FontAwesome.CALENDAR_O, false);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private DashboardViewType(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
