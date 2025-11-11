package Singheatlh.springboot_backend.strategy.reschedule;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory for selecting the appropriate appointment reschedule strategy.
 * Following Factory Pattern and Strategy Pattern.
 * Uses the isStaff flag in RescheduleContext to determine which strategy to use.
 *
 * The strategy map is cached at initialization for performance.
 */
@Component
@RequiredArgsConstructor
public class RescheduleStrategyFactory {

    private final List<AppointmentRescheduleStrategy> strategies;
    private Map<String, AppointmentRescheduleStrategy> strategyMap;

    /**
     * Initialize the strategy map once during bean creation.
     * This avoids rebuilding the map on every request.
     */
    @PostConstruct
    public void initStrategyMap() {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        AppointmentRescheduleStrategy::getStrategyName,
                        Function.identity()
                ));
    }

    /**
     * Get the appropriate reschedule strategy based on the context.
     * @param context The reschedule context containing user role information
     * @return The appropriate strategy (PatientRescheduleStrategy or StaffRescheduleStrategy)
     */
    public AppointmentRescheduleStrategy getStrategy(RescheduleContext context) {
        // Select strategy based on isStaff flag
        if (context.isStaff()) {
            return strategyMap.get("STAFF");
        } else {
            return strategyMap.get("PATIENT");
        }
    }
}
