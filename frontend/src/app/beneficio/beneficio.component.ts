import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { BeneficioService } from './beneficio.service';

export interface Beneficio {
  id?: number;
  nome: string;
  descricao?: string;
  valor: number;
  ativo?: boolean;
}

@Component({
  selector: 'app-beneficio',
  templateUrl: './beneficio.component.html',
  styleUrls: ['./beneficio.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyPipe, RouterLink]
})
export class BeneficioComponent implements OnInit {
  beneficios: Beneficio[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';

  // modal state
  modalAberto = false;
  modalBeneficio: Beneficio = { nome: '', descricao: '', valor: 0, ativo: true };
  modalModo: 'criar' | 'editar' = 'criar';

  constructor(private beneficioService: BeneficioService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    const erroTransferencia = sessionStorage.getItem('transferencia_erro');
    if (erroTransferencia) {
      this.errorMessage = erroTransferencia;
      sessionStorage.removeItem('transferencia_erro');
    }
    this.load();
  }

  load(): void {
    this.loading = true;
    this.beneficioService.list().subscribe({
      next: data => { this.beneficios = data; this.loading = false; this.cdr.detectChanges(); },
      error: () => { this.errorMessage = 'Erro ao carregar benefícios'; this.loading = false; this.cdr.detectChanges(); }
    });
  }

  abrirModalIncluir(): void {
    this.modalBeneficio = { nome: '', descricao: '', valor: 0, ativo: true };
    this.modalModo = 'criar';
    this.modalAberto = true;
    this.errorMessage = '';
  }

  abrirModalEditar(b: Beneficio): void {
    this.modalBeneficio = { ...b };
    this.modalModo = 'editar';
    this.modalAberto = true;
    this.errorMessage = '';
  }

  cancelarModal(): void {
    this.modalAberto = false;
  }

  confirmarModal(): void {
    if (!this.modalBeneficio.nome || !this.modalBeneficio.valor) {
      this.errorMessage = 'Nome e Valor são obrigatórios';
      return;
    }
    if (this.modalModo === 'criar') {
      this.beneficioService.create(this.modalBeneficio).subscribe({
        next: () => {
          this.successMessage = 'Benefício criado com sucesso!';
          this.modalAberto = false;
          this.load();
          this.cdr.detectChanges();
          setTimeout(() => { this.successMessage = ''; this.cdr.detectChanges(); }, 3000);
        },
        error: () => { this.errorMessage = 'Erro ao criar benefício'; this.cdr.detectChanges(); }
      });
    } else {
      this.beneficioService.update(this.modalBeneficio.id!, this.modalBeneficio).subscribe({
        next: () => {
          this.successMessage = 'Benefício atualizado com sucesso!';
          this.modalAberto = false;
          this.load();
          this.cdr.detectChanges();
          setTimeout(() => { this.successMessage = ''; this.cdr.detectChanges(); }, 3000);
        },
        error: () => { this.errorMessage = 'Erro ao atualizar benefício'; this.cdr.detectChanges(); }
      });
    }
  }

  excluir(id: number): void {
    if (!confirm('Confirma a exclusão deste benefício?')) return;
    this.beneficioService.delete(id).subscribe({
      next: () => {
        this.beneficios = this.beneficios.filter(b => b.id !== id);
        this.successMessage = 'Benefício excluído com sucesso!';
        this.cdr.detectChanges();
        setTimeout(() => { this.successMessage = ''; this.cdr.detectChanges(); }, 3000);
      },
      error: () => { this.errorMessage = 'Erro ao excluir benefício'; }
    });
  }
}
