package hu.fnf.devel.forex.states;

import hu.fnf.devel.forex.Signal;
import hu.fnf.devel.forex.StateMachine;

import java.util.HashSet;
import java.util.Set;

import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;

public class SignalSeekerState extends State {

	public SignalSeekerState() {
		super("SignalSeekerState");
	}

	@Override
	public Set<State> nextStates() {
		// TODO: make more logic
		Set<State> ret = new HashSet<State>();
		ret.add(new ScalpHolderState());
		return ret;
	}

	@Override
	public boolean onArriving() {
		LOGGER.info("subscribing to instruments:");
		instruments = new HashSet<Instrument>();
		for (State s : nextStates()) {
			for (Instrument i : s.getInstruments()) {
				instruments.add(i);
			}
		}

		for (Instrument i : instruments) {
			LOGGER.info("\t*" + i.name());
		}

		StateMachine.getInstance().getContext().setSubscribedInstruments(instruments);
		return true;
	}

	@Override
	public boolean onLeaving() {
		// removing references
		this.commands = null;
		this.instruments = null;
		this.periods = null;
		this.signal = null;
		return true;
	}

	@Override
	public Signal signalStrength(Instrument instrument, ITick tick, State actual) {
		LOGGER.debug(getName() + " signalStrength calculation "); 
		if (actual instanceof ScalpHolderState ) {
			return actual.signalStrength(instrument, tick, null);
		}
		return new Signal();
	}

}
