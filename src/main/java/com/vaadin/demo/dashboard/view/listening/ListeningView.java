package com.vaadin.demo.dashboard.view.listening;

import java.io.File;

import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Audio;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
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
//		addStyleName("reports");
//		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR); 
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
//		addComponent(mp3Layout);
//		addComponent(showPDF());
		addComponent(loadTree());
	}

	Component loadTree() {
		final Object[][] planets = new Object[][] {
				new Object[] { "Mercury" },
				new Object[] { "Venus" },
				new Object[] { "Earth", "The Moon" },
				new Object[] { "Mars", "Phobos", "Deimos" },
				new Object[] { "Jupiter", "Io", "Europa", "Ganymedes",
						"Callisto" },
				new Object[] { "Saturn", "Titan", "Tethys", "Dione", "Rhea",
						"Iapetus" },
				new Object[] { "Uranus", "Miranda", "Ariel", "Umbriel",
						"Titania", "Oberon" },
				new Object[] { "Neptune", "Triton", "Proteus", "Nereid",
						"Larissa" } };

		Tree tree = new Tree("The Planets and Major Moons");

		/* Add planets as root items in the tree. */
		for (int i = 0; i < planets.length; i++) {
			String planet = (String) (planets[i][0]);
			tree.addItem(planet);

			if (planets[i].length == 1) {
				// The planet has no moons so make it a leaf.
				tree.setChildrenAllowed(planet, false);
			} else {
				// Add children (moons) under the planets.
				for (int j = 1; j < planets[i].length; j++) {
					String moon = (String) planets[i][j];

					// Add the item as a regular item.
					tree.addItem(moon);

					// Set it to be a child.
					tree.setParent(moon, planet);

					// Make the moons look like leaves.
					tree.setChildrenAllowed(moon, false);
				}

				// Expand the subtree.
				tree.expandItemsRecursively(planet);
			}
			tree.setIcon(FontAwesome.TREE);
		}
		return tree;
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
		// pdf.addComponent(e);
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
