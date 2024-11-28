import estilos from "./ModalRemoverAtribuicao.module.css";

function ModalRemoverAtribuicao({ onClose, onConfirm, permissaoNome }){
    return(
        <div className={estilos.modalOverlayAtribuicao}>
        <div className={estilos.modalContentAtribuicao}>
          <h2>Confirmar Exclusão</h2>
          <p>Tem certeza que deseja excluir a permissão "{permissaoNome}"?</p>
          
          <div className={estilos.actionsAtribuicao}>
            <button onClick={onClose} className={estilos.cancelButtonAtribuicao}>
              Cancelar
            </button>
            <button onClick={onConfirm} className={estilos.deleteButtonAtribuicao}>
              Confirmar
            </button>
          </div>
        </div>
      </div>
    )
}

export default ModalRemoverAtribuicao;