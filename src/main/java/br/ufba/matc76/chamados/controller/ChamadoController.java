package br.ufba.matc76.chamados.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

import br.ufba.matc76.chamados.model.Chamado;
import br.ufba.matc76.chamados.model.Prioridade;
import br.ufba.matc76.chamados.model.Status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final List<Chamado> chamados = new ArrayList<>(
        List.of(
            new Chamado(1L, "Impressora não imprime", "Ana Souza", "Financeiro", Prioridade.ALTA, Status.ABERTO),
            new Chamado(2L, "Esqueci a senha do e-mail", "Carlos Lima", "RH", Prioridade.BAIXA, Status.RESOLVIDO),
            new Chamado(3L, "Computador não liga", "Fernanda Alves", "Vendas", Prioridade.ALTA, Status.EM_ANDAMENTO),
            new Chamado(4L, "Internet lenta no setor", "Roberto Dias", "Marketing", Prioridade.MEDIA, Status.ABERTO),
            new Chamado(5L, "Instalar o Excel no notebook novo", "Juliana Costa", "Compras", Prioridade.BAIXA, Status.EM_ANDAMENTO),
            new Chamado(6L, "Impressora com papel enroscado", "Paulo Mendes", "RH", Prioridade.MEDIA, Status.RESOLVIDO),
            new Chamado(7L, "Sem acesso ao sistema financeiro", "Marta Rocha", "Financeiro", Prioridade.ALTA, Status.EM_ANDAMENTO),
            new Chamado(8L, "Rede caiu na sala de reunião", "Diego Martins", "Diretoria", Prioridade.ALTA, Status.RESOLVIDO),
            new Chamado(9L, "Trocar mouse quebrado", "Larissa Gomes", "Vendas", Prioridade.BAIXA, Status.ABERTO),
            new Chamado(10L, "VPN não conecta", "Bruno Teixeira", "Compras", Prioridade.ALTA, Status.ABERTO),
            new Chamado(11L, "Monitor piscando", "Camila Ribeiro", "Marketing", Prioridade.MEDIA, Status.EM_ANDAMENTO),
            new Chamado(12L, "Criar conta de acesso para novo estagiário", "Eduardo Pinto", "RH", Prioridade.MEDIA, Status.ABERTO)
        )
    );

    @GetMapping
    public List<Chamado> listar(){
        return chamados;
    }

    @GetMapping("/{id}")
    public Chamado buscarPorId(@PathVariable Long id){
        return chamados.stream()
            .filter(chamado -> chamado.id().equals(id))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Chamado não encontrado"
            ));
    }

    @GetMapping("/setor")
    public List<Chamado> filtrarPorSetor(@RequestParam String nome){
        return chamados.stream()
            .filter(chamado -> chamado.setor().equalsIgnoreCase(nome))
            .toList();
    }

    @GetMapping("/prioridade")
    public List<Chamado> filtrarPorPrioridade(@RequestParam String nivel){
        return chamados.stream()
            .filter(chamado -> chamado.prioridade().name().equalsIgnoreCase(nivel))
            .toList();
    }

    @GetMapping("/busca")
    public List<Chamado> buscarPorTitulo(@RequestParam String texto){
        return chamados.stream()
            .filter(chamado -> chamado.titulo()
                .toLowerCase()
                .contains(texto.toLowerCase())
            )
            .toList();
    }

    @GetMapping("/criticos")
    public List<Chamado> listarCriticos(){
        return chamados.stream()
            .filter(chamado -> chamado.prioridade() == Prioridade.ALTA
                && chamado.status() != Status.RESOLVIDO)
            .toList();
    }

    @GetMapping("/resumo")
    public Map<String, Long> resumo(){
        Map<String, Long> resumo = new LinkedHashMap<>();
        resumo.put("abertos", contarPorStatus(Status.ABERTO));
        resumo.put("emAndamento", contarPorStatus(Status.EM_ANDAMENTO));
        resumo.put("resolvidos", contarPorStatus(Status.RESOLVIDO));
        resumo.put("total", (long) chamados.size());
        return resumo;
    }

    private long contarPorStatus(Status status){
        return chamados.stream()
            .filter(chamado -> chamado.status() == status)
            .count();
    }
}
