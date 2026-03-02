import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

interface Beneficio {
  id?: number;
  nome: string;
  descricao?: string;
  valor: number;
  ativo?: boolean;
}

@Injectable({ providedIn: 'root' })
export class BeneficioService {
  private apiUrl = 'http://localhost:8080/api/v1/beneficios';

  constructor(private http: HttpClient) { }

  list(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.apiUrl);
  }

  create(b: Beneficio): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.apiUrl, b);
  }

  update(id: number, b: Beneficio): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.apiUrl}/${id}`, b);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { observe: 'response' });
  }

  transfer(fromId: number, toId: number, amount: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/transfer`, {}, {
      params: new HttpParams()
        .set('fromId', fromId.toString())
        .set('toId', toId.toString())
        .set('amount', amount.toString())
    });
  }
}
