import Tooltip from "@mui/material/Tooltip";
import { useProfessors } from "../context/ProfessorContext";

function ProfessorName({ name }) {
  const profMap = useProfessors();
  const data = profMap[name];

  return (
    <Tooltip
      title={
        data
          ? `⭐ ${data.rating} | Difficulty: ${data.difficulty} (${data.numRatings} ratings)`
          : "No rating available"
      }
    >
      <span style={{ cursor: "pointer", textDecoration: "underline" }}>
        {name}
      </span>
    </Tooltip>
  );
}

export default ProfessorName;