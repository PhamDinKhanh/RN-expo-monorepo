//Define types/interfaces for data exchanged between JavaScript and native code.
export interface PokemonData {
    name: string;
    detailUrl: string;
}

export interface PokemonPageResponse {
    count: number;
    next: string | null;
    previous: string | null;
    results: PokemonData[];
}