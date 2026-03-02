import { Routes } from '@angular/router';
import { BeneficioComponent } from './beneficio/beneficio.component';
import { TransferenciaComponent } from './transferencia/transferencia.component';

export const routes: Routes = [
  { path: '', redirectTo: 'beneficios', pathMatch: 'full' },
  { path: 'beneficios', component: BeneficioComponent },
  { path: 'transferencia', component: TransferenciaComponent }
];
