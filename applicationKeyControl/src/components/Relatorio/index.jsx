import React, { useState, useEffect } from "react";
import axios from "axios";
import estilos from "./Relatorio.module.css";
import jsPDF from "jspdf";
import html2canvas from "html2canvas";
import * as XLSX from "xlsx";

function Relatorio() {
    const [mostrarFiltro, setMostrarFiltro] = useState(false);
    const [filtros, setFiltros] = useState({
        periodoInicio: "",
        periodoFim: "",
        vigilante: "",
        emprestadoPor: "",
        devolvidoPor: "",
        chave: "",
        atraso: "",
    });

    const [dados, setDados] = useState([]);
    const [registrosPorPagina, setRegistrosPorPagina] = useState(10);
    const [paginaAtual, setPaginaAtual] = useState(1);

    useEffect(() => {
        const fetchEmprestimos = async () => {
            try {
                const response = await axios.get("http://localhost:8081/visualizar-emprestimos");
                const emprestimos = response.data.map((item) => ({
                    emprestimo: new Date(`${item.dataRetirada}T${item.horaRetirada}`),
                    chave: item.chave.codigo,
                    emprestadoPor: item.solicitante ? item.solicitante.nome : "",
                    vigilanteEmprestimo: item.vigilanteRetirada ? item.vigilanteRetirada.nome : "",
                    devolucao: item.dataEntrega ? new Date(`${item.dataEntrega}T${item.horaEntrega}`) : "",
                    devolvidoPor: item.devolvente ? item.devolvente.nome: "",
                    vigilanteDevolucao: item.vigilanteEntrega ? item.vigilanteEntrega.nome : "",
                    atraso: item.atraso ? "Sim" : "Não",
                }));
                setDados(emprestimos);
            } catch (error) {
                console.error("Erro ao buscar os empréstimos:", error);
            }
        };

        fetchEmprestimos();
    }, []); 

    const inicio = (paginaAtual - 1) * registrosPorPagina;
    const fim = inicio + registrosPorPagina;

    const dadosFiltrados = dados.filter((item) => {
        return (
            (!filtros.periodoInicio || new Date(item.emprestimo) >= new Date(filtros.periodoInicio)) &&
            (!filtros.periodoFim || new Date(item.devolucao) <= new Date(filtros.periodoFim)) &&
            (!filtros.vigilante || item.vigilanteEmprestimo.includes(filtros.vigilante)) &&
            (!filtros.emprestadoPor || item.emprestadoPor.includes(filtros.emprestadoPor)) &&
            (!filtros.devolvidoPor || item.devolvidoPor.includes(filtros.devolvidoPor)) &&
            (!filtros.chave || String(item.chave).includes(filtros.chave)) &&
            (!filtros.atraso || item.atraso === filtros.atraso)
        );
    });

    const dadosPagina = dadosFiltrados.slice(inicio, fim);
    const totalPaginas = Math.ceil(dadosFiltrados.length / registrosPorPagina);

    const mudarPagina = (pagina) => {
        setPaginaAtual(pagina);
    };

    const mudarRegistrosPorPagina = (e) => {
        setRegistrosPorPagina(Number(e.target.value));
        setPaginaAtual(1);
    };

    const atualizarFiltro = (e) => {
        const { name, value } = e.target;
        setFiltros({ ...filtros, [name]: value });
    };

    const gerarPDF = () => {
        const input = document.getElementById("tabelaRelatorio");
        html2canvas(input, { scale: 2 }).then((canvas) => {
            const imgData = canvas.toDataURL("image/png");
            const pdf = new jsPDF("landscape", "mm", "a4");

            const pdfWidth = pdf.internal.pageSize.getWidth();
            const pdfHeight = (canvas.height * pdfWidth) / canvas.width;

            pdf.addImage(imgData, "PNG", 0, 0, pdfWidth, pdfHeight);
            pdf.save("relatorio.pdf");
        });
    };

    const gerarExcel = () => {
        const dadosExportar = dadosFiltrados.map((item) => ({
            "Dia/Hora do Empréstimo": item.emprestimo.toLocaleString(),
            Chave: item.chave,
            "Emprestado por": item.emprestadoPor,
            Vigilante: item.vigilanteEmprestimo,
            "Dia/Hora da Devolução": item.devolucao ? item.devolucao.toLocaleString() : "N/A",
            "Devolvido por": item.devolvidoPor,
            "Vigilante (Devolução)": item.vigilanteDevolucao,
            Atraso: item.atraso,
        }));

        const worksheet = XLSX.utils.json_to_sheet(dadosExportar);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "Relatório");

        XLSX.writeFile(workbook, "relatorio.xlsx");
    };


    return (
        <div>
            <div className={estilos.container}>
                <div className={estilos.cabecalho}>
                    <h1>Relatórios</h1>
                    <div
                        className={estilos.filtro}
                        onClick={() => setMostrarFiltro(!mostrarFiltro)}
                    >
                        Filtrar ▼
                    </div>
                </div>

                {mostrarFiltro && (
                    <div className={estilos.filtroContainer}>
                        <form className={estilos.formularioFiltros}>
                            <div>
                                <label>Período Início:</label>
                                <input
                                    type="datetime-local"
                                    name="periodoInicio"
                                    value={filtros.periodoInicio}
                                    onChange={atualizarFiltro}
                                />
                            </div>
                            <div>
                                <label>Período Fim:</label>
                                <input
                                    type="datetime-local"
                                    name="periodoFim"
                                    value={filtros.periodoFim}
                                    onChange={atualizarFiltro}
                                />
                            </div>
                            <div>
                                <label>Vigilante:</label>
                                <input
                                    type="text"
                                    name="vigilante"
                                    value={filtros.vigilante}
                                    onChange={atualizarFiltro}
                                />
                            </div>
                            <div>
                                <label>Emprestado por:</label>
                                <input
                                    type="text"
                                    name="emprestadoPor"
                                    value={filtros.emprestadoPor}
                                    onChange={atualizarFiltro}
                                />
                            </div>
                            <div>
                                <label>Devolvido por:</label>
                                <input
                                    type="text"
                                    name="devolvidoPor"
                                    value={filtros.devolvidoPor}
                                    onChange={atualizarFiltro}
                                />
                            </div>
                            <div>
                                <label>Chave:</label>
                                <input
                                    type="text"
                                    name="chave"
                                    value={filtros.chave}
                                    onChange={atualizarFiltro}
                                />
                            </div>
                            <div>
                                <label>Atraso:</label>
                                <select
                                    name="atraso"
                                    value={filtros.atraso}
                                    onChange={atualizarFiltro}
                                >
                                    <option value="">Todos</option>
                                    <option value="Sim">Sim</option>
                                    <option value="Não">Não</option>
                                </select>
                            </div>
                        </form>
                    </div>
                )}

                <hr className={estilos.divisoria} />

                <div className={estilos.tabelaContainer}>
                    <table id="tabelaRelatorio" className={estilos.tabela}>
                        <thead>
                            <tr>
                                <th>Dia/Hora do Empréstimo</th>
                                <th>Chave</th>
                                <th>Emprestado por</th>
                                <th>Vigilante</th>
                                <th>Dia/Hora da devolução</th>
                                <th>Devolvido por</th>
                                <th>Vigilante</th>
                                <th>Atraso</th>
                            </tr>
                        </thead>
                        <tbody>
                            {dadosPagina.map((linha, index) => (
                                <tr key={index}>
                                    <td>{linha.emprestimo ? new Date(linha.emprestimo).toLocaleString() : "N/A"}</td>
                                    <td>{linha.chave}</td>
                                    <td>{linha.emprestadoPor}</td>
                                    <td>{linha.vigilanteEmprestimo}</td>
                                    <td>{linha.devolucao ? new Date(linha.devolucao).toLocaleString() : "N/A"}</td>
                                    <td>{linha.devolvidoPor}</td>
                                    <td>{linha.vigilanteDevolucao}</td>
                                    <td>{linha.atraso}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>


                <div className={estilos.paginacao}>
                    <span>
                        {Array.from({ length: totalPaginas }, (_, i) => i + 1).map((pagina) => (
                            <button
                                key={pagina}
                                className={pagina === paginaAtual ? estilos.ativo : ""}
                                onClick={() => mudarPagina(pagina)}
                            >
                                {pagina}
                            </button>
                        ))}
                    </span>

                    <div>
                        Registros por página:
                        <select onChange={mudarRegistrosPorPagina} value={registrosPorPagina}>
                            <option>10</option>
                            <option>20</option>
                            <option>30</option>
                        </select>
                    </div>
                </div>
                <div className={estilos.botaoContainer}>
                    <button className={estilos.botaoExportar} onClick={gerarPDF}>
                        Exportar PDF
                    </button>
                    <button className={estilos.botaoExportar} onClick={gerarExcel}>
                        Exportar Excel
                    </button>
                </div>
            </div>
        </div>
    );
}

export default Relatorio;
