import estilos from "./RelatorioAdm.module.css"
import Relatorio from "../../components/Relatorio";
import TopoAdministrador from "../../components/TopoAdministrador";

function RelatorioAdm(){
    return(
        <div>
            <TopoAdministrador/>
            <Relatorio/>
        </div>
    )
}

export default RelatorioAdm;