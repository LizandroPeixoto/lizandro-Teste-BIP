import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { BeneficioService } from '../beneficio/beneficio.service';

@Component({
  selector: 'app-transferencia',
  templateUrl: './transferencia.component.html',
  styleUrls: ['./transferencia.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink]
})
export class TransferenciaComponent {
  fromId: number | null = null;
  toId: number | null = null;
  amount: number | null = null;

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private beneficioService: BeneficioService) {}

  transferir(): void {
    if (!this.fromId || !this.toId || !this.amount) {
      this.errorMessage = 'Preencha todos os campos obrigatórios.';
      return;
    }
    if (this.fromId === this.toId) {
      this.errorMessage = 'Os IDs de origem e destino não podem ser iguais.';
      return;
    }
    if (this.amount <= 0) {
      this.errorMessage = 'O valor deve ser maior que zero.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.beneficioService.transfer(this.fromId, this.toId, this.amount).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Transferência realizada com sucesso!';
        setTimeout(() => { window.location.href = '/beneficios'; }, 1000);
      },
      error: (err) => {
        const msg: string = err?.error?.message || err?.error?.error || err?.message || 'Erro ao realizar transferência.';
        this.loading = false;
        this.errorMessage = msg;
        const isSaldoInsuficiente = msg.toLowerCase().includes('saldo') || msg.toLowerCase().includes('insuficiente');
        if (isSaldoInsuficiente) {
          sessionStorage.setItem('transferencia_erro', msg);
          setTimeout(() => { window.location.href = '/beneficios'; }, 2000);
        }
      }
    });
  }
}
