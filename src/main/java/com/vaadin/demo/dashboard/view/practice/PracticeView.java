package com.vaadin.demo.dashboard.view.practice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.eventbus.Subscribe;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.demo.dashboard.DashboardUI;
import com.vaadin.demo.dashboard.component.MovieDetailsWindow;
import com.vaadin.demo.dashboard.domain.Movie;
import com.vaadin.demo.dashboard.domain.MusicMedia;
import com.vaadin.demo.dashboard.domain.MusicMediaType;
import com.vaadin.demo.dashboard.domain.PracticeItem;
import com.vaadin.demo.dashboard.domain.Transaction;
import com.vaadin.demo.dashboard.event.DashboardEvent.BrowserResizeEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
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
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class PracticeView extends VerticalLayout implements View,
		ItemClickListener {

	private Calendar calendar;
	private PracticeItem selectedPracticeItem;

	// private final Component tray;

	public PracticeView() {
		setSizeFull();
		setMargin(true);
		addStyleName("schedule");
		DashboardEventBus.register(this);

		TabSheet tabs = new TabSheet();
		tabs.setSizeFull();
		tabs.addStyleName(ValoTheme.TABSHEET_FRAMED);

		tabs.addComponent(buildGeneralPraticeView());
		tabs.addComponent(buildSongPracticeView());

		addComponent(tabs);

		// tray = buildTray();
		// addComponent(tray);

		// injectMovieCoverStyles();
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
		final Object ICON_PROPERTY = "icon";
		// Create new container
		HierarchicalContainer prContainer = new HierarchicalContainer();
		// Create containerproperty for name
		prContainer.addContainerProperty(TYPE_PROPERTY, String.class, null);
		prContainer.addContainerProperty(ICON_PROPERTY, Resource.class, null);
		prContainer.addContainerProperty("details", PracticeItem.class, null);
		Map<String, ArrayList<PracticeItem>> practiceItems = DashboardUI
				.getDataProvider().getPracticeItems();
		for (String key : practiceItems.keySet()) {
			int typeId = itemId;
			item = prContainer.addItem(typeId);
			item.getItemProperty(TYPE_PROPERTY).setValue(key);
			item.getItemProperty(ICON_PROPERTY).setValue(FontAwesome.MUSIC);
			prContainer.setChildrenAllowed(typeId, true);
			for (Iterator<PracticeItem> iterator = practiceItems.get(key)
					.iterator(); iterator.hasNext();) {
				PracticeItem practiceItem = iterator.next();
				int nameId = ++itemId;
				item = prContainer.addItem(nameId);
				item.getItemProperty(TYPE_PROPERTY).setValue(
						practiceItem.getName());
				item.getItemProperty("details").setValue(practiceItem);
				item.getItemProperty(ICON_PROPERTY).setValue(
						FontAwesome.PAPERCLIP);
				prContainer.setParent(nameId, typeId);
				prContainer.setChildrenAllowed(nameId, false);
			}
			++itemId;
		}
		return prContainer;
	}

	Component generalPracticeMenuPanel() {
		Panel panel = new Panel("Your Practice List");
		panel.addStyleName("color2");
		panel.setWidthUndefined();
		panel.setContent(generalPracticeMenuPanelContent());
		return panel;
	}

	Component generalPracticeMenuPanelContent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(createMenuTree());

		return layout;
	}

	protected Component createMenuTree() {
		Tree menu = new Tree();
		menu.addContainerProperty("caption", String.class, "");
		menu.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		menu.setItemCaptionPropertyId("caption");
		menu.setContainerDataSource(getPracticeItemContainer());
		menu.setItemCaptionPropertyId("practice");
		// menu.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		menu.setItemIconPropertyId("icon");
		menu.expandItem(getPracticeItemContainer().getItemIds().iterator()
				.next());
		menu.setImmediate(true);
		menu.setVisible(true);
		menu.setEnabled(true);
		menu.setSelectable(true);
		menu.setDragMode(TreeDragMode.NODE);
		menu.setSizeFull();
		menu.addItemClickListener(this);
		menu.addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedPracticeItem = (PracticeItem) event.getItem()
						.getItemProperty("details").getValue();
				
//				generalPracticeItemDetailsContent();
				itemContainer();

			}
		});
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
		HorizontalLayout generalPracticeLayout = new HorizontalLayout();
		generalPracticeLayout.setCaption("General Practice");
		addStyleName(ValoTheme.UI_WITH_MENU);
		generalPracticeLayout.setMargin(true);
		generalPracticeLayout.addComponent(generalPracticeMenuPanel());
		generalPracticeLayout
				.addComponent(generalPracticeItemDetails(selectedPracticeItem));
		return generalPracticeLayout;
	}

	private Component generalPracticeItemDetails(PracticeItem item) {
		Panel panel = new Panel("Details");
		panel.addStyleName("color2");
		panel.setWidthUndefined();
		panel.setContent(generalPracticeItemDetailsContent());
		return panel;
	}

	IndexedContainer c = new IndexedContainer();
	
	@SuppressWarnings("unchecked")
	private Container itemContainer() {
		c.removeAllItems();
		c.addContainerProperty("name", String.class, null);
		c.addContainerProperty("url", String.class, null);
		c.addContainerProperty("type", MusicMediaType.class, null);
		int id = 0;
		if (selectedPracticeItem != null) {
			for (MusicMedia m : selectedPracticeItem.getMaterials()) {
				Item item = c.addItem(id);
				item.getItemProperty("name").setValue(m.getName());
				item.getItemProperty("url").setValue(m.getUrl());
				item.getItemProperty("type").setValue(m.getType());
				id++;
//				c.addValueChangeListener(listener);
			}
		}

		return c;

	}

	private Component generalPracticeItemDetailsContent() {
		VerticalLayout select = new VerticalLayout();
		select.setMargin(true);
		final OptionGroup options = new OptionGroup("Choose:");
		options.addStyleName("large");
		options.setMultiSelect(true);
		options.setContainerDataSource(c);
		options.addItemSetChangeListener(new ItemSetChangeListener() {

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				// TODO Auto-generated method stub
				options.setItemCaptionPropertyId("name");
				
				options.setContainerDataSource(c);
			}

		});
		options.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				options.setItemCaptionPropertyId("name");
				
				options.setContainerDataSource(c);
				
			}
		});
		// options.addItem("Option One");
		// options.addItem("Option Two");
		// options.addItem("Option Three");
		// options.select("Option One");
		// options.setItemIcon("Option One", FontAwesome.PENCIL);
		// options.setItemIcon("Option Two", FontAwesome.VIDEO_CAMERA);
		// options.setItemIcon("Option Three", FontAwesome.YOUTUBE);
		select.addComponent(options);

		Button button = new Button("Go");
		button.setSizeFull();

		select.addComponent(button);
		return select;
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
				// setTrayVisible(false);
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

	// private void setTrayVisible(final boolean visible) {
	// final String styleReveal = "v-animate-reveal";
	// if (visible) {
	// tray.addStyleName(styleReveal);
	// } else {
	// tray.removeStyleName(styleReveal);
	// }
	// }

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
