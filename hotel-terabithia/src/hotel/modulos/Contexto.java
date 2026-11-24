package hotel.modulos;

import hotel.dominio.EstadoHotel;
import hotel.util.Terminal;

public record Contexto(Terminal terminal, String usuario, EstadoHotel estado) {
}
