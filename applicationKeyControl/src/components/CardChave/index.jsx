import estilos from "./CardChave.module.css";

function CardChave({ number, title, status, onClick }) {
    return (
        <div className={estilos.card} onClick={onClick}>
            <div className={estilos.header}>
                <img
                    src={status ? "icon_true.png" : "icon_false.png"}
                    alt={status ? "Desbloqueado" : "Bloqueado"}
                    className={estilos.statusIcon}
                />
            </div>
            <span className={estilos.number}>{number}</span>
            <div className={estilos.title}>{title}</div>
        </div>
    );
}

export default CardChave;
