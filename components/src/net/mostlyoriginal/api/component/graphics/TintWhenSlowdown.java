package net.mostlyoriginal.api.component.graphics;

import com.artemis.Component;
import com.artemis.annotations.Fluid;
import com.badlogic.gdx.graphics.Color;

/**
 * Colorize for animations, labels.
 * <p/>
 * Optional, convention is to assume white if not set.
 *
 * @see Color
 * @author Daan van Yperen
 */
@Fluid(swallowGettersWithParameters=true)
public class TintWhenSlowdown extends Component {
	public Color normal	= new Color();
	public Color slow = new Color();
}
