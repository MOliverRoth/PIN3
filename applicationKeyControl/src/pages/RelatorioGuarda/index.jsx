import React, { useState } from "react";
import estilos from "./RelatorioGuarda.module.css";
import TopoGuarda from "../../components/TopoGuarda";
import Relatorio from "../../components/Relatorio";

function RelatorioGuarda() {
    return (
        <div>
            <TopoGuarda/>
            <Relatorio/>
        </div>
    )
    
}

export default RelatorioGuarda;
