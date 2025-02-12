package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.remote.dataobjects.RestActivityBookingData;

@Controller
@RequestMapping(value = "/providers/{codeProvider}/activities/{codeActivity}/offers/{externalId}/bookings")
public class BookingController {
	private static Logger logger = LoggerFactory.getLogger(BookingController.class);

	private static final ActivityInterface activityInterface = new ActivityInterface();

	@RequestMapping(method = RequestMethod.GET)
	public String offerBookingsPage(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity,
			@PathVariable String externalId) {
		logger.info("offerBookingsPage codeProvider:{}, codeActivity:{}, externalId:{}", codeProvider, codeActivity,
				externalId);

		ActivityOfferData activityOfferData = activityInterface.getActivityOfferDataByExternalId(externalId);

		if (activityOfferData == null) {
			model.addAttribute("error", "Error: it does not exist an offer");
			model.addAttribute("provider", new ActivityProviderData());
			model.addAttribute("providers", ActivityInterface.getProviders());
			return "providers";
		} else {
			model.addAttribute("booking", new RestActivityBookingData());
			model.addAttribute("offer", activityOfferData);
			return "bookings";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String bookingSubmit(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity,
			@PathVariable String externalId, @ModelAttribute RestActivityBookingData booking) {
		logger.info("offerSubmit codeProvider:{}, codeActivity:{}, externalId:{}", codeProvider, codeActivity,
				externalId);

		try {
			activityInterface.reserveActivity(externalId, booking);
		} catch (ActivityException e) {
			model.addAttribute("error", "Error: it was not possible to do the booking");
			model.addAttribute("booking", booking);
			model.addAttribute("offer", activityInterface.getActivityOfferDataByExternalId(externalId));
			return "bookings";
		}

		return "redirect:/providers/" + codeProvider + "/activities/" + codeActivity + "/offers/" + externalId
				+ "/bookings";
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public String bookingCancel(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity,
								@PathVariable String externalId, @ModelAttribute RestActivityBookingData booking, @RequestParam String reference) {
		try {
			activityInterface.cancelReservation(reference);
		} catch(ActivityException ae) {
			model.addAttribute("error", "Error: it was not possible to do the booking");
			model.addAttribute("booking", booking);
			model.addAttribute("offer", activityInterface.getActivityOfferDataByExternalId(externalId));
			return "bookings";
		}
		return "redirect:/providers/" + codeProvider + "/activities/" + codeActivity + "/offers/" + externalId
				+ "/bookings";
	}

}
