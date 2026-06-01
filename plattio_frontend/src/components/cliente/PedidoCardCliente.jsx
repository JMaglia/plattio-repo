import '../../styles/PedidoCardCliente.css';

const PedidoCardCliente = ({ pedido, numero, modo, onConfirmar }) => {
    const mostrarBotonConfirmar = modo === "mozo" && pedido.estado === "a_confirmar";

    const confirmarPedido = (id) => {
        fetch(`http://localhost:8080/pedidos/${id}/cambiarEstado/pendiente`, {
            method: "POST"
        })
            .then(() => window.location.reload());
    };

    const cancelarPedido = (id) => {
        fetch(`http://localhost:8080/pedidos/${id}/cambiarEstado/cancelado`, {
            method: "POST"
        })
            .then(() => window.location.reload());
    };

    const formatearEstado = (estado) => {
        if (!estado) return "";
        return estado
            .replace(/_/g, " ")               // reemplaza _ por espacio
            .replace(/\b\w/g, c => c.toUpperCase()); // capitaliza cada palabra
    };



    return (
        <div className="pedido-card-cliente">
            <div className="pedido-header">
                <span>{numero}</span>
                <span className="estado">{formatearEstado(pedido.estado)}</span>
            </div>

            {pedido.items.map((item, i) => (
                <div key={i} className="plato">
                    <div>
                        <strong>{item.nombre}</strong>
                        <div className="detalle">{item.detalle}</div>
                    </div>
                    <div>x{item.cantidad}</div>
                </div>
            ))}

            {mostrarBotonConfirmar && (
                <div className="botones-confirmacion">
                    <><button className="confirmar-btn" onClick={() => confirmarPedido(pedido.id)}>
                        Confirmar
                    </button>
                        <button className="cancelar-btn" onClick={() => cancelarPedido(pedido.id)}>
                            Cancelar
                        </button></>
                </div>
            )}
        </div>
    );
};

export default PedidoCardCliente;
