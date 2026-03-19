import React, { useState, useEffect } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "react-big-calendar/lib/css/react-big-calendar.css";

const localizer = momentLocalizer(moment);

/* TIME STEPPER COMPONENT */

function TimeStepper({ value, onChange }) {
  const { hour, minute } = value;

  function changeHour(delta) {
    let newHour = (hour + delta + 24) % 24;
    onChange({ hour: newHour, minute });
  }

  function changeMinute(delta) {
    let total = hour * 60 + minute + delta;

    if (total < 0) total += 24 * 60;
    if (total >= 24 * 60) total -= 24 * 60;

    const newHour = Math.floor(total / 60);
    const newMinute = total % 60;

    onChange({ hour: newHour, minute: newMinute });
  }

  return (
    <div style={{ display: "flex", alignItems: "center", gap: "5px" }}>
      <div style={{ textAlign: "center" }}>
        <button onClick={() => changeHour(1)}>▲</button>
        <div>{String(hour).padStart(2, "0")}</div>
        <button onClick={() => changeHour(-1)}>▼</button>
      </div>

      <div>:</div>

      <div style={{ textAlign: "center" }}>
        <button onClick={() => changeMinute(30)}>▲</button>
        <div>{String(minute).padStart(2, "0")}</div>
        <button onClick={() => changeMinute(-30)}>▼</button>
      </div>
    </div>
  );
}

/* MAIN COMPONENT */

export default function CourseSearch() {

  const [courses, setCourses] = useState([]);
  const [selectedCourses, setSelectedCourses] = useState([]);

  const [showCalendar, setShowCalendar] = useState(false);

  const [search, setSearch] = useState("");
  const [dept, setDept] = useState("");
  const [credits, setCredits] = useState("");
  const [prof, setProf] = useState("");
  const [isfull, setFull] = useState("");
  const [days, setDays] = useState([]);

  const [startTime, setStartTime] = useState({ hour: 8, minute: 0 });
  const [endTime, setEndTime] = useState({ hour: 17, minute: 0 });

  const [errorMessage, setErrorMessage] = useState("");
  const [errorCourseKey, setErrorCourseKey] = useState(null);

  /* LOAD SCHEDULE FROM BACKEND */

  useEffect(() => {
    fetch("http://localhost:7000/schedule")
      .then(res => res.json())
      .then(data => setSelectedCourses(data));
  }, []);

  function toggleDay(day) {
    setDays(prev =>
      prev.includes(day)
        ? prev.filter(d => d !== day)
        : [...prev, day]
    );
  }

  function loadSched() {
    fetch("http://localhost:7000/schedule")
      .then(response => response.json())
      .then(data => {
        setSelectedCourses(data);
      });
  }

  function getEventsFromCourses() {

    const dayMap = { M: 1, T: 2, W: 3, R: 4, F: 5 };

    return selectedCourses.flatMap(course =>
      course.times.map(t => {

        const dayNum = dayMap[t.day];
        const now = new Date();

        // get Sunday of current week
        const startOfWeek = new Date(now);
        startOfWeek.setDate(now.getDate() - now.getDay());

        const start = new Date(
          startOfWeek.getFullYear(),
          startOfWeek.getMonth(),
          startOfWeek.getDate() + dayNum,
          ...t.start_time.split(":").map(Number)
        );

        const end = new Date(
          start.getFullYear(),
          start.getMonth(),
          start.getDate(),
          ...t.end_time.split(":").map(Number)
        );

        return {
          title: `${course.subject}-${course.number}`,
          start,
          end
        };

      })
    );
  }

  /* ADD COURSE USING BACKEND */

  function addCourse(course) {

    fetch("http://localhost:7000/schedule", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(course)
    })
    .then(res => {

      if (res.ok) {
        // success (200–299)
        loadSched();
        setErrorMessage("");
        setErrorCourseKey(null);

      } else if (res.status === 409) {
        // conflict
        setErrorMessage("Time conflict with another course");
        setErrorCourseKey(course.subject + course.number + course.section);

        setTimeout(() => {
          setErrorMessage("");
          setErrorCourseKey(null);
        }, 3000);

      } else {
        // other errors
        setErrorMessage("Failed to add course");
      }

    })
    .catch(() => {
      setErrorMessage("Server error");
    });
  }

  /* REMOVE COURSE */

  function removeCourse(course) {

    fetch("http://localhost:7000/schedule", {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(course)
    })
    .then(() => {
        loadSched();
    });
//     .then(() => {
//
//       setSelectedCourses(prev =>
//   	prev.filter(c => !(c.subject === course.subject && c.number === course.number))
//       );
//
//     });
  }

  /* SAVE SCHEDULE */

  function saveSchedule() {

    const json = JSON.stringify(selectedCourses, null, 2);

    const blob = new Blob([json], { type: "application/json" });
    const url = URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = url;
    a.download = "schedule.json";
    a.click();

    URL.revokeObjectURL(url);
  }

  /* LOAD SCHEDULE */

  function loadSchedule(event) {

    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = function(e) {
      try {
        const data = JSON.parse(e.target.result);
        setSelectedCourses(data);
      } catch {
        alert("Invalid JSON file");
      }
    };

    reader.readAsText(file);
  }

  function formatTime(time) {
    return `${String(time.hour).padStart(2,"0")}:${String(time.minute).padStart(2,"0")}`;
  }

  function searchCourses() {

    const params = new URLSearchParams();

    if (search) params.append("query", search);
    if (dept) params.append("dept", dept);
    if (credits) params.append("credits", credits);
    if (prof) params.append("prof", prof);

    if (isfull) params.append("isFull", isfull);   // FIXED PARAM NAME

    if (startTime) params.append("startTime", formatTime(startTime));
    if (endTime) params.append("endTime", formatTime(endTime));

    if (days.length) params.append("days", days.join(","));

    const url = `http://localhost:7000/search?${params.toString()}`;

    fetch(url)
      .then(res => res.json())
      .then(data => setCourses(data));
  }

  return (

    <div>

        <button
          onClick={() => setShowCalendar(!showCalendar)}
          style={{ margin: "15px" }}
        >
          {showCalendar ? "Back to Search" : "View Calendar"}
        </button>

        {showCalendar ? (

          <div style={{ height: "700px", width: "200%" }}>

            <Calendar
              localizer={localizer}
              events={getEventsFromCourses()}
              startAccessor="start"
              endAccessor="end"
              defaultView="week"
              min={new Date(1970, 1, 1, 7, 0)}
              max={new Date(1970, 1, 1, 22, 0)}
              step={30}
              timeslots={1}
              style={{ height: "700px" }}
            />

          </div>

        ) : (

          <div style={{
            display: "flex",
            justifyContent: "center",
            gap: "40px",
            fontFamily: "Arial"
          }}>

      {/* LEFT SIDE */}

      <div style={{width:"700px"}}>

        <h1>Course Search</h1>

        <input
          placeholder="Search courses..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

        {/* DAY SELECTOR */}

        <div style={{ marginTop: "15px", marginBottom: "10px" }}>

          <div style={{ fontWeight: "bold", marginBottom: "5px" }}>
            Days
          </div>

          <div style={{ display: "flex", gap: "12px" }}>

            {["M","T","W","Th","F"].map(day => (

              <label
                key={day}
                style={{
                  display:"flex",
                  flexDirection:"column",
                  alignItems:"center",
                  cursor:"pointer",
                  fontSize:"14px"
                }}
              >

                <div
                  style={{
                    width:"22px",
                    height:"22px",
                    border:"2px solid #555",
                    borderRadius:"4px",
                    background: days.includes(day) ? "#4CAF50" : "white",
                    marginBottom:"4px",
                    display:"flex",
                    alignItems:"center",
                    justifyContent:"center",
                    color:"white",
                    fontWeight:"bold"
                  }}
                >
                  {days.includes(day) ? "✓" : ""}
                </div>

                {day}

                <input
                  type="checkbox"
                  checked={days.includes(day)}
                  onChange={() => toggleDay(day)}
                  style={{ display:"none" }}
                />

              </label>

            ))}

          </div>

        </div>

        {/* FILTERS */}

        <select value={dept} onChange={(e) => setDept(e.target.value)}>
          <option value="">All Departments</option>
          <option value="COMP">Comp-Sci</option>
          <option value="MATH">Math</option>
          <option value="PHYS">Physics</option>
          <option value="HUMA">Humanities</option>
        </select>

        <select value={credits} onChange={(e) => setCredits(e.target.value)}>
          <option value="">Any Credits</option>
          <option value="1">1 Credit</option>
          <option value="2">2 Credits</option>
          <option value="3">3 Credits</option>
          <option value="4">4 Credits</option>
        </select>

        <select value={prof} onChange={(e) => setProf(e.target.value)}>
          <option value="">Any Professor</option>
          <option value="Hutchins, Jonathan O.">Hutchins</option>
          <option value="Dellinger, Brian J.">Dellinger</option>
        </select>

        <select value={isfull} onChange={(e) => setFull(e.target.value)}>
          <option value="">Both Open & Full</option>
          <option value="true">Full Courses</option>
          <option value="false">Open Courses</option>
        </select>

        {/* TIME FILTERS */}

        <div style={{marginTop:"15px"}}>
          <label>Start Time</label>
          <TimeStepper value={startTime} onChange={setStartTime} />
        </div>

        <div style={{marginTop:"10px"}}>
          <label>End Time</label>
          <TimeStepper value={endTime} onChange={setEndTime} />
        </div>

        <br />

        <button onClick={searchCourses}>Search</button>

        <hr />

        {/* COURSE RESULTS */}

        {courses.map(course => {

          const alreadyAdded = selectedCourses.some(
  	     c => c.subject === course.subject &&
       	     c.number === course.number &&
       	     c.section === course.section
	  );

          return (
            <div
              key={course.code}
              style={{
                border:"1px solid #ddd",
                padding:"10px",
                marginBottom:"10px",
                display:"flex",
                justifyContent:"space-between",
                alignItems:"center"
              }}
            >

              <div>
                <h3>{course.subject} - {course.number}{course.section}</h3>
                <p>{course.name}</p>
                <p>Professor: {course.faculty.join(", ")}</p>
                <p>Credits: {course.credits}</p>
		{Object.values(
    		   course.times.reduce((acc, t) => {
      		      const key = `${t.start_time}-${t.end_time}`;

      		      if (!acc[key]) {
        		acc[key] = { days: "", start: t.start_time, end: t.end_time };
      		      }

      		      acc[key].days += t.day;
      		      return acc;
    		   }, {})
  		).map((t, i) => (
    		   <p key={i}>
      		      {t.days} {t.start}-{t.end}
    		   </p>
  		))}
	      </div>

          <div style={{ position: "relative" }}>

            <button
              onClick={() => addCourse(course)}
              disabled={alreadyAdded}
              style={{
                padding:"6px 12px",
                background: alreadyAdded ? "#aaa" : "#4CAF50",
                color:"white",
                border:"none",
                borderRadius:"4px"
              }}
            >
              {alreadyAdded ? "Added" : "Add"}
            </button>

            {errorCourseKey === (course.subject + course.number + course.section) && (
              <div style={{
                position: "absolute",
                top: "40px",
                right: "0px",
                background: "#ff4d4f",
                color: "white",
                padding: "6px 10px",
                borderRadius: "6px",
                fontSize: "12px",
                whiteSpace: "nowrap",
                boxShadow: "0 2px 6px rgba(0,0,0,0.2)"
              }}>
                {errorMessage}
              </div>
            )}

          </div>

          </div>
          );
        })}

      </div>

      {/* RIGHT SIDE */}

      <div style={{
        width:"260px",
  	border:"1px solid #ddd",
  	padding:"15px",
  	height:"fit-content",
  	borderRadius:"6px",
  	background:"#303030",
  	boxShadow:"0 2px 8px rgba(0,0,0,0.1)"
      }}>

        <h2>My Schedule</h2>

        <div style={{marginBottom:"10px"}}>
          <button onClick={saveSchedule}>Save Schedule</button>

          <label style={{marginLeft:"10px", cursor:"pointer"}}>
            Load Schedule
            <input
              type="file"
              accept=".json"
              onChange={loadSchedule}
              style={{display:"none"}}
            />
          </label>
        </div>

        {selectedCourses.length === 0 && (
          <p style={{color:"#777"}}>No courses added</p>
        )}

        {selectedCourses.map(course => (
          <div
            key={course.subject + course.number}
            style={{
              borderBottom:"1px solid #ddd",
              padding:"8px 0",
              display:"flex",
              justifyContent:"space-between"
            }}
          >

            <div>
              <strong>{`${course.subject}-${course.number}`}</strong>
              <div style={{fontSize:"14px"}}>
                {course.name}
              </div>
            </div>

            <button
              onClick={() => removeCourse(course)}
              style={{
                background:"#d9534f",
                color:"white",
                border:"none",
                padding:"4px 8px",
                borderRadius:"4px"
              }}
            >
              Remove
            </button>

          </div>
        ))}

      </div>

    </div>

    )}

    </div>
  );
}