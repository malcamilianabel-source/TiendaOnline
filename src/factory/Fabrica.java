package factory;

// PATRÓN FACTORY
public class Fabrica {
    public static MetodoPago crearMetodoPago(String tipo) {
        switch (tipo.toLowerCase()) {
            case "tarjeta":
                return new factory.PagoTarjeta();
            case "yape":
                return new factory.PagoYape();
            case "paypal":
                return new factory.PagoPaypal();
            default:
                return null;
        }
    }
}
