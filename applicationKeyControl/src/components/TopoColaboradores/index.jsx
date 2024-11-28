import estilos from "./TopoColaboradores.module.css";

function TopoColaboradores() {
    return (
        <header className={estilos.headerGuarda}>
            <div className={estilos.header_leftGuarda}>
                <a href="/Colaboradores" className={estilos.header_logoGuarda}>KeyGuard</a>
                <nav className={estilos.header_navGuarda}>
                    <a href="/Colaboradores" className={estilos.header_linkGuarda}>Minhas Permissões</a>
                </nav>
            </div>
            <div className={estilos.header_rightGuarda}>
                <button className={estilos.header_iconGuarda}>
                    <img src="icon_perfil.png" alt="Perfil" />
                </button>
            </div>
        </header>
    )
}

export default TopoColaboradores;