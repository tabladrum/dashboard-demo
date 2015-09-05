package com.vaadin.demo.dashboard.view.listening;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.eventbus.Subscribe;
import com.kbdunn.vaadin.addons.mediaelement.MediaElementPlayer;
import com.kbdunn.vaadin.addons.mediaelement.PlaybackEndedListener;
import com.vaadin.demo.dashboard.event.DashboardEvent.ReportsCountUpdatedEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.TransactionReportEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.listening.ReportEditor.PaletteItemType;
import com.vaadin.demo.dashboard.view.listening.ReportEditor.ReportEditorListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Audio;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Video;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class ListeningView extends HorizontalLayout implements View {

	public static final String CONFIRM_DIALOG_ID = "confirm-dialog";
	private String HTML = "<!DOCTYPE html> \n"
			+ "<html lang=\"en\"> \n"
			+ "<head> \n"
			+ "<meta charset=\"utf-8\" \n> "
			+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> \n"
			+ "<meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\"> \n"
			+ "<script type=\"text/javascript\" src=\"http://www.skypeassets.com/i/scom/js/skype-uri.js\"></script> \n"
			+ "<div id=\"SkypeButton_Call_TapabrataPal_1\"> \n"
			+ " <script type=\"text/javascript\"> \n" + "Skype.ui({ \n"
			+ "\"name\": \"dropdown\", \n"
			+ "\"element\": \"SkypeButton_Call_TapabrataPal_1\", \n"
			+ "\"participants\": [\"TapabrataPal\"], \n"
			+ "\"imageSize\": 32 \n" + "}); \n" + "</script> \n" + "</div> \n"
			+ "</html>";

	private String HTML2 = "Skype.ui({ \n" + "\"name\": \"call\" , \n"
			+ "\"element\": \"SkypeButton_Call_TapabrataPal_1\", \n"
			+ "\"participants\": [\"TapabrataPal\"], \n"
			+ "\"imageSize\": 32});";

	public ListeningView() {

		setSizeFull();
		addStyleName("reports");
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		// setCloseHandler(this);
		DashboardEventBus.register(this);
		addComponent(runYouTube());
		final VerticalLayout mp3Layout = new VerticalLayout();
		mp3Layout.addStyleName("mp3");

		final File song1 = new File(
				"/Users/yaf107/Music/iTunes/iTunes Media/Music/Bhupinder Singh/Ghazal by Bhupinder Singh/01 Kya Batayen Humen Kya.m4a");
		final File song = new File(song1.toURI());
		Audio audio = new Audio();
		audio.setSource(new FileResource(song));
		mp3Layout.addComponent(audio);
		addComponent(mp3Layout);
		addComponent(showPDF());
	}

	private void runSkype() {
		Page.getCurrent().getJavaScript()
				.execute("alert('Hello from server side.')");
	}


	private Component showPDF() {
		final VerticalLayout pdf = new VerticalLayout();
		
		Embedded e = new Embedded(null, new FileResource(new File(
				"/Users/yaf107/Downloads/7ReadingSOL2010.pdf")));
		e.setMimeType("application/pdf");
		e.setType(Embedded.TYPE_BROWSER);
		
		e.setParameter("allowFullScreen", "true");
		e.setWidth("320px");
		e.setHeight("265px");
//		pdf.addComponent(e);
		// addComponent(e);
		return e;
	}

	private Component runYouTube() {
		final VerticalLayout utube = new VerticalLayout();
		utube.addStyleName("youtube");
		utube.setSizeFull();
		utube.setCaption("Youtube viedo");
		utube.setSizeUndefined();
		utube.setSpacing(true);

		// MediaElementPlayer yvideoPlayer = new MediaElementPlayer(
		// MediaElementPlayer.Type.VIDEO);
		// utube.addComponent(yvideoPlayer);
		// yvideoPlayer.setSource(new ExternalResource(
		// "https://youtu.be/iCtYXy5odNE"));
		// yvideoPlayer.play();

		Embedded e = new Embedded(null, new ExternalResource(
				"http://www.youtube.com/v/grdNtbqw0VY"));
		e.setMimeType("application/x-shockwave-flash");
		e.setParameter("allowFullScreen", "true");
		e.setWidth("320px");
		e.setHeight("265px");
		// addComponent(e);
		return e;
	}

	private Component buildDrafts() {
		final VerticalLayout allDrafts = new VerticalLayout();
		allDrafts.setSizeFull();
		allDrafts.setCaption("Listening");

		VerticalLayout titleAndDrafts = new VerticalLayout();
		titleAndDrafts.setSizeUndefined();
		titleAndDrafts.setSpacing(true);
		titleAndDrafts.addStyleName("drafts");
		allDrafts.addComponent(titleAndDrafts);
		allDrafts
				.setComponentAlignment(titleAndDrafts, Alignment.MIDDLE_CENTER);

		Label draftsTitle = new Label("Drafts");
		draftsTitle.addStyleName(ValoTheme.LABEL_H1);
		draftsTitle.setSizeUndefined();
		titleAndDrafts.addComponent(draftsTitle);
		titleAndDrafts.setComponentAlignment(draftsTitle, Alignment.TOP_CENTER);

		return allDrafts;
	}


	@Override
	public void enter(final ViewChangeEvent event) {
	}


}
