package net.mostlyoriginal.game.util;

import net.mostlyoriginal.api.util.Cooldown;
import org.junit.jupiter.api.Test;

import static net.mostlyoriginal.api.utils.Duration.seconds;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daan van Yperen
 */
class CooldownTest {

    private static final float A_SECOND = 1f;

    @Test
    void When_new_cooldown_Should_match_interval() {
        float startingInterval = seconds(5f);
        assertEquals(startingInterval, Cooldown.withInterval(startingInterval).get());
    }

    @Test
    void When_cooldown_reaches_zero_Should_trigger() {
        assertTrue(Cooldown.withInterval(A_SECOND).decreaseBy(A_SECOND).ready());
        assertTrue(Cooldown.withInterval(A_SECOND).ready(A_SECOND));
    }

    @Test
    void When_ready_Should_automatically_reset_by_default() {
        Cooldown cooldown = Cooldown.withInterval(A_SECOND);
        cooldown.ready(A_SECOND);
        assertTrue(cooldown.get() > 0);
    }

    @Test
    void When_manually_decreasing_Should_not_reset() {
        Cooldown cooldown = Cooldown.withInterval(A_SECOND);
        cooldown.decreaseBy(A_SECOND);
        assertTrue(cooldown.get() <= 0);
    }

    @Test
    void When_ready_and_auto_reset_off_Should_not_reset() {
        Cooldown cooldown = Cooldown.withInterval(A_SECOND).autoReset(false);
        cooldown.ready(2f);
        assertTrue(cooldown.get() <= 0);
    }

    @Test
    void When_overflow_mode_active_and_should_subtract_it_from_nextcooldown() {
        Cooldown cooldown = Cooldown.withInterval(A_SECOND).subtractOverflowFromNextCooldown(true);
        cooldown.ready(1.1f); // will underflow the cooldown by .1 second.
        assertEquals(0.9f, cooldown.get(),0.01f); // the next cooldown is expected to be this.
    }

    @Test
    void When_overflow_mode_active_and_is_larger_than_interval_Should_stay_at_zero() {
        Cooldown cooldown = Cooldown.withInterval(A_SECOND).subtractOverflowFromNextCooldown(true);
        cooldown.ready(5f); // will underflow the cooldown by .1 second.
        assertEquals(0f, cooldown.get(),0.01f); // the next cooldown is expected to be this.
    }

    @Test
    void When_overflow_Should_not_subtract_by_default() {
        Cooldown cooldown = Cooldown.withInterval(A_SECOND);
        cooldown.ready(1.1f); // will underflow the cooldown by .1 second.
        assertEquals(A_SECOND, cooldown.get(),0.01f); // the next cooldown is expected to be this.
    }
}