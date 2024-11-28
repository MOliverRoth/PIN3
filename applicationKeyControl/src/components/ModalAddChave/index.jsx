import { useState } from "react";
import estilos from "./ModalAddChave.module.css";

function ModalAddChave({ onClose, onCreate }) {
    const [codigo, setCodigo] = useState("");
    const [descricao, setDescricao] = useState("");
    const [status, setStatus] = useState("DISPONÍVEL"); 
    const [horaInicio, setHoraInicio] = useState("");
    const [horaFim, setHoraFim] = useState("");
    const [dias, setDias] = useState({
        segunda: false,
        terca: false,
        quarta: false,
        quinta: false,
        sexta: false,
        sabado: false,
        domingo: false,
    });

    const handleDiaChange = (dia) => {
        setDias((prevDias) => ({
            ...prevDias,
            [dia]: !prevDias[dia],
        }));
    };

    const handleCreate = () => {
        const formatHora = (hora) => {
            if (!hora) return null;
            return hora.includes(":") ? `${hora}:00` : `${hora}:00:00`;
        };
    
        const novaChave = {
            codigo: parseInt(codigo),
            descricao,
            status,
            horaInicio: formatHora(horaInicio),
            horaFim: formatHora(horaFim),
            ...dias,
        };
    
        console.log("Dados enviados para criação de chave:", novaChave); 
    
        onCreate(novaChave);
        onClose();
    };
    
    
    return (
        <div className={estilos.modalOverlay}>
            <div className={estilos.modalContent}>
                <button onClick={onClose} className={estilos.closeButton}>
                    &times;
                </button>

                <h2>Nova Chave</h2>

                <label className={estilos.label}>
                    Código:
                    <input
                        type="number"
                        value={codigo}
                        onChange={(e) => setCodigo(e.target.value)}
                        className={estilos.input}
                        placeholder="Digite o código"
                    />
                </label>

                <label className={estilos.label}>
                    Descrição:
                    <textarea
                        value={descricao}
                        onChange={(e) => setDescricao(e.target.value)}
                        className={estilos.textarea}
                        placeholder="Digite uma descrição"
                    />
                </label>

                <label className={estilos.label}>
                    Horário de início:
                    <input
                        type="time"
                        value={horaInicio}
                        onChange={(e) => setHoraInicio(e.target.value)}
                        className={estilos.input}
                    />
                </label>

                <label className={estilos.label}>
                    Horário de fim:
                    <input
                        type="time"
                        value={horaFim}
                        onChange={(e) => setHoraFim(e.target.value)}
                        className={estilos.input}
                    />
                </label>

                <fieldset className={estilos.label}>
                    <legend>Dias da semana:</legend>
                    {Object.keys(dias).map((dia) => (
                        <label key={dia} className={estilos.checkboxLabel}>
                            <input
                                type="checkbox"
                                checked={dias[dia]}
                                onChange={() => handleDiaChange(dia)}
                            />
                            {dia.charAt(0).toUpperCase() + dia.slice(1)}
                        </label>
                    ))}
                </fieldset>

                <div className={estilos.actions}>
                    <button onClick={onClose} className={estilos.cancelButton}>
                        Cancelar
                    </button>
                    <button onClick={handleCreate} className={estilos.createButton}>
                        Criar
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ModalAddChave;
