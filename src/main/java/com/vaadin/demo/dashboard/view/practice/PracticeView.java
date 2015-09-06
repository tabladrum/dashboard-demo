package com.vaadin.demo.dashboard.view.practice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.demo.dashboard.DashboardUI;
import com.vaadin.demo.dashboard.component.MovieDetailsWindow;
import com.vaadin.demo.dashboard.domain.Movie;
import com.vaadin.demo.dashboard.domain.PracticeItem;
import com.vaadin.demo.dashboard.domain.Transaction;
import com.vaadin.demo.dashboard.event.DashboardEvent.BrowserResizeEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class PracticeView extends VerticalLayout implements View, ItemClickListener {

	private Calendar calendar;
//	private final Component tray;

	public PracticeView() {
		setSizeFull();
		setMargin(true);
//		addStyleName("schedule");
////		addStyleName(ValoTheme.UI_WITH_MENU);
		DashboardEventBus.register(this);
		
//		TabSheet tabs = new TabSheet();
//		tabs.setSizeFull();
//		tabs.addStyleName(ValoTheme.TABSHEET_ONLY_SELECTED_TAB_IS_CLOSABLE);

//		tabs.addComponent(buildGeneralPraticeView());
//		tabs.addComponent(buildSongPracticeView());

//		addComponent(tabs);
		addComponent(buildGeneralPraticeView());
//		addComponent(createMenuTree());
//		tray = buildTray();
//		addComponent(tray);
		
//		injectMovieCoverStyles();
	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of ScheduleView is created every time it's navigated
		// to so we'll need to clean up references to it on detach.
		DashboardEventBus.unregister(this);
	}

	@SuppressWarnings("unchecked")
	private static HierarchicalContainer getPracticeItemContainer() {
		Item item = null;
		int itemId = 0; // Increasing numbering for itemId:s

		final Object TYPE_PROPERTY = "practice";
		// final Object NAME_PROPERTY = "name";
		// final Object DESC_PROPERTY = "description";
		// final Object URL_PROPERTY = "url";
		// Create new container
		HierarchicalContainer prContainer = new HierarchicalContainer();
		// Create containerproperty for name
		prContainer.addContainerProperty(TYPE_PROPERTY, String.class, null);
		// prContainer.addContainerProperty(NAME_PROPERTY, String.class, null);
		// prContainer.addContainerProperty(DESC_PROPERTY, String.class, null);
		// prContainer.addContainerProperty(URL_PROPERTY, String.class, null);
		Map<String, ArrayList<PracticeItem>> practiceItems = DashboardUI
				.getDataProvider().getPracticeItems();
		for (String key : practiceItems.keySet()) {
			int typeId = itemId;
			item = prContainer.addItem(typeId);
			item.getItemProperty(TYPE_PROPERTY).setValue(key);
			prContainer.setChildrenAllowed(typeId, true);
			for (Iterator<PracticeItem> iterator = practiceItems.get(key)
					.iterator(); iterator.hasNext();) {
				PracticeItem practiceItem = iterator.next();
				int nameId = ++itemId;
				item = prContainer.addItem(nameId);
				item.getItemProperty(TYPE_PROPERTY).setValue(
						practiceItem.getName());
				prContainer.setParent(nameId, typeId);
				prContainer.setChildrenAllowed(nameId, false);
			}
			++itemId;
		}
		return prContainer;
	}

	Component generalPracticeMenuPanel() {
        Panel panel = new Panel("Custom Caption");
        panel.addStyleName("color3");
        panel.setContent(generalPracticeMenuPanelContent());
        return panel;
	}

	Component generalPracticeMenuPanelContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        Label content = new Label(
                "Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio.");
        content.setWidth("10em");
        layout.addComponent(content);
        layout.addComponent(createMenuTree());
        
        Button button = new Button("Button");
        button.setSizeFull();
        layout.addComponent(button);
        return layout;
    }
	
	protected Component createMenuTree() {
		Tree menu = new Tree("Your practice menu");
		menu.addContainerProperty("caption", String.class, "");
		menu.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		menu.setItemCaptionPropertyId("caption");
		menu.setContainerDataSource(getPracticeItemContainer());
		menu.setItemCaptionPropertyId("practice");
		menu.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		menu.expandItem(getPracticeItemContainer().getItemIds().iterator().next());
		menu.setImmediate(true);
		menu.setVisible(true);
		menu.setEnabled(true);
		menu.setSelectable(true);
		menu.setDragMode(TreeDragMode.NODE);
		menu.setSizeFull();
		menu.addItemClickListener(this);

		return menu;
	}
	



	private void injectMovieCoverStyles() {
		// Add all movie cover images as classes to CSSInject
		String styles = "";
		for (Movie m : DashboardUI.getDataProvider().getMovies()) {
			WebBrowser webBrowser = Page.getCurrent().getWebBrowser();

			String bg = "url(VAADIN/themes/" + UI.getCurrent().getTheme()
					+ "/img/event-title-bg.png), url(" + m.getThumbUrl() + ")";

			// IE8 doesn't support multiple background images
			if (webBrowser.isIE() && webBrowser.getBrowserMajorVersion() == 8) {
				bg = "url(" + m.getThumbUrl() + ")";
			}

			styles += ".v-calendar-event-" + m.getId()
					+ " .v-calendar-event-content {background-image:" + bg
					+ ";}";
		}

		Page.getCurrent().getStyles().add(styles);
	}

	private Component buildGeneralPraticeView() {
		VerticalLayout generalPracticeLayout = new VerticalLayout();
		generalPracticeLayout.setCaption("General Practice");
		addStyleName(ValoTheme.UI_WITH_MENU);
		generalPracticeLayout.setMargin(true);
		generalPracticeLayout.addComponent(generalPracticeMenuPanel());
		return generalPracticeLayout;
	}

	private Component buildSongPracticeView() {
		CssLayout catalog = new CssLayout();
		catalog.setCaption("Song Practice");
		catalog.addStyleName("catalog");

		for (final Movie movie : DashboardUI.getDataProvider().getMovies()) {
			VerticalLayout frame = new VerticalLayout();
			frame.addStyleName("frame");
			frame.setWidthUndefined();

			Image poster = new Image(null, new ExternalResource(
					movie.getThumbUrl()));
			poster.setWidth(100.0f, Unit.PIXELS);
			poster.setHeight(145.0f, Unit.PIXELS);
			frame.addComponent(poster);

			Label titleLabel = new Label(movie.getTitle());
			titleLabel.setWidth(120.0f, Unit.PIXELS);
			frame.addComponent(titleLabel);

			frame.addLayoutClickListener(new LayoutClickListener() {
				@Override
				public void layoutClick(final LayoutClickEvent event) {
					if (event.getButton() == MouseButton.LEFT) {
						MovieDetailsWindow.open(movie, null, null);
					}
				}
			});
			catalog.addComponent(frame);
		}
		return catalog;
	}

	private Component buildTray() {
		final HorizontalLayout tray = new HorizontalLayout();
		tray.setWidth(100.0f, Unit.PERCENTAGE);
		tray.addStyleName("tray");
		tray.setSpacing(true);
		tray.setMargin(true);

		Label warning = new Label(
				"You have unsaved changes made to the schedule");
		warning.addStyleName("warning");
		warning.addStyleName("icon-attention");
		tray.addComponent(warning);
		tray.setComponentAlignment(warning, Alignment.MIDDLE_LEFT);
		tray.setExpandRatio(warning, 1);

		ClickListener close = new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
//				setTrayVisible(false);
			}
		};

		Button confirm = new Button("Confirm");
		confirm.addStyleName(ValoTheme.BUTTON_PRIMARY);
		confirm.addClickListener(close);
		tray.addComponent(confirm);
		tray.setComponentAlignment(confirm, Alignment.MIDDLE_LEFT);

		Button discard = new Button("Discard");
		discard.addClickListener(close);
		discard.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				calendar.markAsDirty();
			}
		});
		tray.addComponent(discard);
		tray.setComponentAlignment(discard, Alignment.MIDDLE_LEFT);
		return tray;
	}

//	private void setTrayVisible(final boolean visible) {
//		final String styleReveal = "v-animate-reveal";
//		if (visible) {
//			tray.addStyleName(styleReveal);
//		} else {
//			tray.removeStyleName(styleReveal);
//		}
//	}

	@Subscribe
	public void browserWindowResized(final BrowserResizeEvent event) {
		if (Page.getCurrent().getBrowserWindowWidth() < 800) {
			calendar.setEndDate(calendar.getStartDate());
		}
	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	private class MovieEventProvider implements CalendarEventProvider {

		@Override
		public List<CalendarEvent> getEvents(final Date startDate,
				final Date endDate) {
			// Transactions are dynamically fetched from the backend service
			// when needed.
			Collection<Transaction> transactions = DashboardUI
					.getDataProvider().getTransactionsBetween(startDate,
							endDate);
			List<CalendarEvent> result = new ArrayList<CalendarEvent>();
			for (Transaction transaction : transactions) {
				Movie movie = DashboardUI.getDataProvider().getMovie(
						transaction.getMovieId());
				Date end = new Date(transaction.getTime().getTime()
						+ movie.getDuration() * 60 * 1000);
				result.add(new MovieEvent(transaction.getTime(), end, movie));
			}
			return result;
		}
	}

	public final class MovieEvent implements CalendarEvent {

		private Date start;
		private Date end;
		private Movie movie;

		public MovieEvent(final Date start, final Date end, final Movie movie) {
			this.start = start;
			this.end = end;
			this.movie = movie;
		}

		@Override
		public Date getStart() {
			return start;
		}

		@Override
		public Date getEnd() {
			return end;
		}

		@Override
		public String getDescription() {
			return "";
		}

		@Override
		public String getStyleName() {
			return String.valueOf(movie.getId());
		}

		@Override
		public boolean isAllDay() {
			return false;
		}

		public Movie getMovie() {
			return movie;
		}

		public void setMovie(final Movie movie) {
			this.movie = movie;
		}

		public void setStart(final Date start) {
			this.start = start;
		}

		public void setEnd(final Date end) {
			this.end = end;
		}

		@Override
		public String getCaption() {
			return movie.getTitle();
		}

	}
	

	@Override
	public void itemClick(ItemClickEvent event) {
		// TODO Auto-generated method stub
		
	}

}
