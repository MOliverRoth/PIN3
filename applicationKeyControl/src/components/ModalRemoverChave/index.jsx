import React from "react";
import estilos from "./ModalRemoverChave.module.css"; 

function ModalRemoverChave({ onClose, onConfirm, permissaoNome }) {
  return (
    <div className={estilos.modalOverlay}>
      <div className={estilos.modalContent}>
        <h2>Confirmar Exclusão</h2>
        <p>Tem certeza que deseja excluir a chave "{permissaoNome}"?</p>
        
        <div className={estilos.actions}>
          <button onClick={onClose} className={estilos.cancelButton}>
            Cancelar
          </button>
          <button onClick={onConfirm} className={estilos.deleteButton}>
            Confirmar
          </button>
        </div>
      </div>
    </div>
  );
}

export default ModalRemoverChave;
