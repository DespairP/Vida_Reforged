package teamHTBP.vidaReforged.core.api.capability;

public class VidaCapabilityResult<T> {
    final Result result;
    final T message;

    public VidaCapabilityResult(Result result, T message) {
        this.result = result;
        this.message = message;
    }
}
