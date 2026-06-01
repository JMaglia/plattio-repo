import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginEmpleado from './components/LoginEmpleado';
import CocinaBoard from './components/CocinaBoard';
import MesaView from './components/cliente/MesaView';
import CartaCliente from './components/cliente/CartaCliente';
import ResumenPedido from './components/cliente/ResumenPedido';
import MozoBoard from './components/mozo/MozoBoard';
import MozoPedidos from './components/mozo/MozoPedidos';
import NotificacionesMozo from './components/mozo/NotificacionesMozo';
import EditarCarta from './components/mozo/EditarCarta';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginEmpleado />} />
        <Route path="/cocina" element={<CocinaBoard />} />
        <Route path="/mesa/:numeroMesa" element={<MesaView />} />
        <Route path="/mesa/:numeroMesa/carta" element={<CartaCliente />} />
        <Route path="/mesa/:numeroMesa/resumen" element={<ResumenPedido />} />
        <Route path="*" element={<h2>Página no encontrada</h2>} />
        <Route path="/mozo/:idMozo" element={<MozoBoard />} />
        <Route path="/mozo/pedidos/:idMozo" element={<MozoPedidos />} />
        <Route path="/notificaciones/:idMozo" element={<NotificacionesMozo />} />
        <Route path="/editarcarta" element={<EditarCarta />} />
      </Routes>
    </Router>
  );
}

export default App;
