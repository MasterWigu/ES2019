package pt.ulisboa.tecnico.softeng.car.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.RentACarInterface;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

@Controller
@RequestMapping(value = "/rentacars/rentacar/{code}/pendings")
public class PendingRentingController {
	private static final Logger logger = LoggerFactory.getLogger(RentingsController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String pendingForm(Model model, @PathVariable String code) {
		logger.debug("rentingForm");

		RentACarInterface rentACarInterface = new RentACarInterface();

		RentACarData rentACarData = rentACarInterface.getRentACarData(code);
		if (rentACarData == null) {
			model.addAttribute("error", "Error: it does not exist a rent-a-car with code " + code);
			model.addAttribute("rentacar", rentACarInterface.getRentACarData(code));
			model.addAttribute("pendings", rentACarInterface.getPendings(code));
			model.addAttribute("pending", new RentingData());
			return "pendingView";
		} else {
			model.addAttribute("rentacar", rentACarInterface.getRentACarData(code));
			model.addAttribute("pendings", rentACarInterface.getPendings(code));
			model.addAttribute("pending", new RentingData());
			return "pendingView";
		}
	}

}