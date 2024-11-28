import { BrowserRouter, Route, Routes } from "react-router-dom";
import Inicio from './pages/Inicio';
import Login from './pages/login';
import Chave from "./pages/Chave";
import RelatorioAdm from "./pages/RelatorioAdm";
import RelatorioGuarda from "./pages/RelatorioGuarda";
import ItemChave from "./pages/ItemChave";
import InicioColaboradores from "./pages/InicioColaboradores";

function AppRoutes(){
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}></Route>
                <Route path="/Inicio" element={<Inicio/>}></Route>
                <Route path="/Relatorio" element={<RelatorioGuarda/>}></Route>
                <Route path="/Chaves" element={<Chave/>}></Route>
                <Route path="/RelatorioAdm" element={<RelatorioAdm/>}></Route>
                <Route path="/chave/:id" element={<ItemChave />} />
                <Route path="/Colaboradores" element={<InicioColaboradores/>}></Route>
            </Routes>
        </BrowserRouter>
    )
}

export default AppRoutes