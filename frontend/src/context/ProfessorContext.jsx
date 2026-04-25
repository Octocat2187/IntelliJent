import { createContext, useContext, useEffect, useState } from "react";
import { supabase } from "../supabaseClient";

const ProfessorContext = createContext();

export function ProfessorProvider({ children }) {
  const [profMap, setProfMap] = useState({});

  useEffect(() => {
    async function load() {
      const { data, error } = await supabase
        .from("Professors")
        .select("name, rmp_rating, rmp_difficulty, rmp_num_ratings");

      if (error) {
        console.error(error);
        return;
      }

      const map = {};
      data.forEach(row => {
        map[row.name] = {
          rating: row.rmp_rating,
          difficulty: row.rmp_difficulty,
          numRatings: row.rmp_num_ratings
        };
      });

      setProfMap(map);
    }

    load();
  }, []);

  return (
    <ProfessorContext.Provider value={profMap}>
      {children}
    </ProfessorContext.Provider>
  );
}

export function useProfessors() {
  return useContext(ProfessorContext);
}