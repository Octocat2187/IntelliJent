import React, { useState, useEffect } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "react-big-calendar/lib/css/react-big-calendar.css";

const localizer = momentLocalizer(moment);

/* TIME STEPPER COMPONENT */

function TimeStepper({ value, onChange }) {
  const { hour, minute } = value;

  function changeByMinutes(delta) {
    let total = hour * 60 + minute + delta;

    if (total < 0) total += 24 * 60;
    if (total >= 24 * 60) total -= 24 * 60;

    const newHour = Math.floor(total / 60);
    const newMinute = total % 60;

    onChange({ hour: newHour, minute: newMinute });
  }

  function handleTimeInputChange(e) {
    const input = e.target.value;
    if (!input) return;

    const parts = input.split(":");
    if (parts.length !== 2) return;

    const parsedHour = Number(parts[0]);
    const parsedMinute = Number(parts[1]);

    if (
      Number.isNaN(parsedHour) ||
      Number.isNaN(parsedMinute) ||
      parsedHour < 0 ||
      parsedHour > 23 ||
      parsedMinute < 0 ||
      parsedMinute > 59
    ) {
      return;
    }

    onChange({ hour: parsedHour, minute: parsedMinute });
  }

  function handleWheel(e) {
    e.preventDefault();
    const delta = e.deltaY < 0 ? 30 : -30;
    changeByMinutes(delta);
  }

  const timeInputValue = `${String(hour).padStart(2, "0")}:${String(minute).padStart(2, "0")}`;

  return (
    <div style={{ display: "inline-flex", alignItems: "center", gap: "6px", marginLeft: "8px" }}>
      <input
        type="time"
        value={timeInputValue}
        onChange={handleTimeInputChange}
        onWheel={handleWheel}
        step={1800}
        aria-label="Time input"
      />
      <div style={{ display: "flex", flexDirection: "column", gap: "2px" }}>
        <button
          type="button"
          onClick={() => changeByMinutes(30)}
          aria-label="Increase time"
          style={{ width: "24px", lineHeight: 1, padding: "2px 0" }}
        >
          +
        </button>
        <button
          type="button"
          onClick={() => changeByMinutes(-30)}
          aria-label="Decrease time"
          style={{ width: "24px", lineHeight: 1, padding: "2px 0" }}
        >
          -
        </button>
      </div>
    </div>
  );
}

function RequiredCoursesPage({
  majors,
  selectedMajorName,
  requiredCourses,
  majorsError,
  onSelectMajor,
}) {
  return (
    <div
      style={{
        maxWidth: "900px",
        margin: "0 auto",
        padding: "0 20px 20px 20px",
        fontFamily: "Arial"
      }}
    >
      <h1 style={{ margin: "0 0 10px 0" }}>Required Courses</h1>
      <p style={{ margin: "0 0 20px 0" }}>
        Select a major to view its required courses.
      </p>

      <div style={{ marginBottom: "20px" }}>
        <select
          value={selectedMajorName}
          onChange={(e) => onSelectMajor(e.target.value)}
          style={{ minWidth: "280px", padding: "10px" }}
        >
          <option value="">Choose a major</option>
          {majors.map((major) => (
            <option key={major.name} value={major.name}>
              {major.name}
            </option>
          ))}
        </select>
      </div>

      {majorsError && (
        <div
          style={{
            marginBottom: "20px",
            padding: "12px",
            borderRadius: "8px",
            background: "#ffe5e5",
            color: "#8a1f1f",
          }}
        >
          {majorsError}
        </div>
      )}

      {selectedMajorName && (
        <div
          style={{
            border: "1px solid #ddd",
            borderRadius: "10px",
            padding: "20px",
          }}
        >
          <h2 style={{ margin: "0 0 12px 0" }}>{selectedMajorName}</h2>

          {requiredCourses.length > 0 ? (
            <ul style={{ margin: 0, paddingLeft: "20px" }}>
              {requiredCourses.map((course) => (
                <li key={course} style={{ marginBottom: "8px" }}>
                  {course}
                </li>
              ))}
            </ul>
          ) : (
            <p style={{ margin: 0 }}>No required courses found for this major.</p>
          )}
        </div>
      )}
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

  const [showAlternativesModal, setShowAlternativesModal] = useState(false);
  const [alternatives, setAlternatives] = useState([]);
  const [failedCourse, setFailedCourse] = useState(null);

  const [showFullNotification, setShowFullNotification] = useState(false);
  const [fullCourseName, setFullCourseName] = useState("");

  const [allCourses, setAllCourses] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [professors, setProfessors] = useState([]);

  const [showRequiredCourses, setShowRequiredCourses] = useState(false);
  const [majors, setMajors] = useState([]);
  const [selectedMajorName, setSelectedMajorName] = useState("");
  const [requiredCourses, setRequiredCourses] = useState([]);
  const [majorsError, setMajorsError] = useState("");

  const [showLogin, setShowLogin] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loggedInUser, setLoggedInUser] = useState(null);
  const [loginError, setLoginError] = useState("");
  const [signupError, setSignupError] = useState("");
  const [signupSuccess, setSignupSuccess] = useState("");

  /* LOAD SCHEDULE FROM BACKEND */

  useEffect(() => {
    if (!loggedInUser) {
      setSelectedCourses([]);
      return;
    }

    fetch(`http://localhost:7000/schedule?username=${encodeURIComponent(loggedInUser)}`)
      .then(res => res.json())
      .then(data => setSelectedCourses(data));
  }, [loggedInUser]);

  useEffect(() => {
    fetch("http://localhost:7000/search?") // all of the courses, since no query params
      .then(res => res.json())
      .then(data => {
        setAllCourses(data);
        extractDropdownData(data);
      });
  }, []);

  useEffect(() => {
    fetch("http://localhost:7000/majors")
      .then((res) => {
        if (!res.ok) {
          throw new Error("Could not load majors from the backend.");
        }
        return res.json();
      })
      .then((data) => {
        setMajors(data);
        setMajorsError("");
      })
      .catch(() => {
        setMajors([]);
        setMajorsError("Could not load majors. Make sure the backend has the major routes enabled.");
      });
  }, []);

  function handleSelectMajor(majorName) {
  setSelectedMajorName(majorName);

  const major = majors.find((m) => m.name === majorName);
  setRequiredCourses(major ? major.requiredCourses : []);
}

  function extractDropdownData(data) {
    const subjectSet = new Set();
    const professorSet = new Set();

    data.forEach(course => {
      // SUBJECT
      if (course.subject) {
        subjectSet.add(course.subject);
      }

      // PROFESSORS (array)
      if (course.faculty) {
        course.faculty.forEach(prof => {
          if (prof && prof.trim() !== "") {
            professorSet.add(prof.trim());
          }
        });
      }
    });

    setSubjects([...subjectSet].sort());
    setProfessors([...professorSet].sort());
  }

  function handleLogin() {
    setLoginError("");

    fetch("http://localhost:7000/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        username,
        password
      })
    })
      .then(async (res) => {
        const data = await res.json();

        if (!res.ok) {
          throw new Error(data.message || "Login failed");
        }

        setLoggedInUser(data.username);
        setShowLogin(false);
        setUsername("");
        setPassword("");
        setLoginError("");
        setSignupError("");
        setSignupSuccess("");
      })
      .catch((err) => {
        setLoginError(err.message);
      });
  }

  function handleSignup() {
    setSignupError("");
    setSignupSuccess("");
    setLoginError("");

    fetch("http://localhost:7000/signup", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        username,
        password
      })
    })
      .then(async (res) => {
        const text = await res.text();
        let data;

        try {
          data = JSON.parse(text);
        } catch {
          throw new Error(text || "Server error");
        }

        if (!res.ok) {
          throw new Error(data.message || "Signup failed");
        }

        setSignupSuccess("Account created successfully. You can now log in.");
        setSignupError("");
        setPassword("");
      })
      .catch((err) => {
        setSignupError(err.message);
        setSignupSuccess("");
      });
  }

  function toggleDay(day) {
    setDays(prev =>
      prev.includes(day)
        ? prev.filter(d => d !== day)
        : [...prev, day]
    );
  }

  function formatTo12Hour(timeStr) {
    if (!timeStr) return "";

    let [hour, minute] = timeStr.split(":").map(Number);

    const ampm = hour >= 12 ? "PM" : "AM";
    hour = hour % 12;
    if (hour === 0) hour = 12;

    return `${hour}:${minute.toString().padStart(2, "0")} ${ampm}`;
  }

  function loadSched() {
    if (!loggedInUser) {
      setSelectedCourses([]);
      return;
    }

    fetch(`http://localhost:7000/schedule?username=${encodeURIComponent(loggedInUser)}`)
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

    if (!loggedInUser) {
      setErrorMessage("Please log in first");
      return;
    }

    fetch(`http://localhost:7000/schedule?username=${encodeURIComponent(loggedInUser)}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(course)
    })
    .then(res => res.json().then(data => ({ status: res.status, data })))
    .then(({ status, data }) => {

      if (status === 201) {
        // success (200–299)
        loadSched();
        setErrorMessage("");
        setErrorCourseKey(null);

        // Show full course notification if applicable
        if (data.courseFull) {
          setFullCourseName(`${course.subject} - ${course.number}${course.section}`);
          setShowFullNotification(true);
          setTimeout(() => setShowFullNotification(false), 3000);
        }

      } else if (status === 409) {
        // conflict - show alternatives
        setFailedCourse(course);
        setAlternatives(data.alternativeCourses || []);
        setShowAlternativesModal(true);

      } else {
        // other errors
        setErrorMessage("Failed to add course");
      }

    })
    .catch(() => {
      setErrorMessage("Server error");
    });
  }

  function addAlternativeCourse(altCourse) {
    if (!loggedInUser) {
      setErrorMessage("Please log in first");
      return;
    }

    fetch(`http://localhost:7000/schedule?username=${encodeURIComponent(loggedInUser)}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(altCourse)
    })
    .then(res => res.json().then(data => ({ status: res.status, data })))
    .then(({ status, data }) => {
      if (status === 201) {
        loadSched();
        setShowAlternativesModal(false);
        setAlternatives([]);
        setFailedCourse(null);

        // Show full course notification if applicable
        if (data.courseFull) {
          setFullCourseName(`${altCourse.subject} - ${altCourse.number}${altCourse.section}`);
          setShowFullNotification(true);
          setTimeout(() => setShowFullNotification(false), 3000);
        }
      } else {
        setErrorMessage("Failed to add alternative course");
        setTimeout(() => setErrorMessage(""), 3000);
      }
    })
    .catch(() => {
      setErrorMessage("Server error");
    });
  }

  /* REMOVE COURSE */

  function removeCourse(course) {

    if (!loggedInUser) {
      return;
    }

    fetch(`http://localhost:7000/schedule?username=${encodeURIComponent(loggedInUser)}`, {
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

        // First, clear the backend schedule
        if (!loggedInUser) {
          alert("Please log in first");
          event.target.value = "";
          return;
        }

        fetch(`http://localhost:7000/schedule/clear?username=${encodeURIComponent(loggedInUser)}`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          }
        })
        .then(() => {
          // Then send loaded courses to backend and wait for all to complete
          const coursePromises = data.map(course =>
            fetch(`http://localhost:7000/schedule?username=${encodeURIComponent(loggedInUser)}`, {
              method: "POST",
              headers: {
                "Content-Type": "application/json"
              },
              body: JSON.stringify(course)
            })
            .catch(() => {
              console.error("Error sending course to backend");
            })
          );

          return Promise.all(coursePromises);
        })
        .then(() => {
          // Update frontend state after all courses are sent
          setSelectedCourses(data);
        })
        .catch(() => {
          console.error("Error clearing backend schedule");
        })
        .finally(() => {
          // Clear the file input so the same file can be loaded again
          event.target.value = "";
        });
      } catch {
        alert("Invalid JSON file");
        event.target.value = "";
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
        <div
          style={{
            position: "fixed",
            top: "20px",
            left: "20px",
            right: "20px",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            zIndex: 1000
          }}
        >

          {/* LEFT SIDE BUTTONS */}
          <div style={{ display: "flex", gap: "10px" }}>
            <button
              onClick={() => {
                const next = !showCalendar;
                setShowCalendar(next);
                if (next) setShowRequiredCourses(false);
              }}
            >
              {showCalendar ? "Back to Search" : "View Calendar"}
            </button>

            <button
              onClick={() => {
                const next = !showRequiredCourses;
                setShowRequiredCourses(next);
                if (next) setShowCalendar(false);
              }}
            >
              {showRequiredCourses ? "Back to Search" : "Required Courses"}
            </button>
          </div>

          {/* RIGHT SIDE ACCOUNT */}
          <div>
            {loggedInUser ? (
              <button
                onClick={() => {
                  setLoggedInUser(null);
                  setSelectedCourses([]);
                }}
              >
                {loggedInUser} (Logout)
              </button>
            ) : (
              <button
                onClick={() => {
                  setShowLogin(true);
                  setLoginError("");
                  setSignupError("");
                  setSignupSuccess("");
                  setUsername("");
                  setPassword("");
                }}
              >
                Account
              </button>
            )}
          </div>

        </div>


      <div style={{ height: "80px" }} />

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

        ) : showRequiredCourses ? (
            <RequiredCoursesPage
              majors={majors}
              selectedMajorName={selectedMajorName}
              requiredCourses={requiredCourses}
              majorsError={majorsError}
              onSelectMajor={handleSelectMajor}
            />
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
          {subjects.map(sub => (
              <option key={sub} value={sub}>
                {sub}
              </option>
          ))}
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
          {professors.map(p => (
              <option key={p} value={p}>
                {p}
              </option>
          ))}
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
       	     c.section === course.section &&
       	     c.semester === course.semester
	  );

          return (
            <div
              key={course.subject + course.number + course.section + course.semester}
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
                <p>Seats open: {course.open_seats}</p>
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
      		      {t.days} {formatTo12Hour(t.start)} - {formatTo12Hour(t.end)}
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

            {errorCourseKey === (course.subject + course.number + course.section + course.semester) && (
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

      <div className="schedule-container">

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
              justifyContent:"space-between",
              alignItems:"flex-start",
              gap:"8px"
            }}
          >

            <div style={{flex:1, minWidth:0}}>
              <strong>{`${course.subject}-${course.number}${course.section}`}</strong>
              <div style={{fontSize:"14px"}}>
                {course.name}

                {Object.values(
                  course.times.reduce((acc, t) => {
                    const key = `${t.start_time}-${t.end_time}`;

                    if (!acc[key]) {
                      acc[key] = {
                        days: "",
                        start: t.start_time,
                        end: t.end_time
                      };
                    }

                    acc[key].days += t.day;
                    return acc;
                  }, {})
                ).map((t, i) => (
                  <div key={i}>
                    {t.days} {formatTo12Hour(t.start)} - {formatTo12Hour(t.end)}
                  </div>
                ))}
              </div>
            </div>

            <button
              onClick={() => removeCourse(course)}
              style={{
                background:"#d9534f",
                color:"white",
                border:"none",
                padding:"4px 8px",
                borderRadius:"4px",
                flexShrink:0,
                whiteSpace:"nowrap"
              }}
            >
              Remove
            </button>

          </div>
        ))}

      </div>

    </div>

    )}

    {/* FULL COURSE NOTIFICATION */}

    {showFullNotification && (
      <div style={{
        position: "fixed",
        top: "20px",
        right: "20px",
        background: "#ffc107",
        color: "#333",
        padding: "12px 16px",
        borderRadius: "6px",
        boxShadow: "0 4px 12px rgba(0, 0, 0, 0.2)",
        zIndex: "999",
        fontWeight: "500",
        maxWidth: "300px"
      }}>
        ⚠️ Full Course Added: {fullCourseName}
      </div>
    )}

    {/* ALTERNATIVES MODAL */}

    {showAlternativesModal && (
      <div style={{
        position: "fixed",
        top: "0",
        left: "0",
        right: "0",
        bottom: "0",
        background: "rgba(0, 0, 0, 0.8)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        zIndex: "1000"
      }}>

        <div style={{
          background: "gray",
          padding: "20px",
          borderRadius: "8px",
          width: "90%",
          maxWidth: "600px",
          maxHeight: "80%",
          overflowY: "auto",
          position: "relative"
        }}>

          <button
            onClick={() => setShowAlternativesModal(false)}
            style={{
              position: "absolute",
              top: "10px",
              right: "10px",
              background: "#aaaaaa",
              border: "none",
              fontSize: "18px",
              cursor: "pointer"
            }}
          >
            &times;
          </button>

          <h2 style={{ marginTop: "0" }}>Course Conflict</h2>

          <p>
            The course <strong>{failedCourse?.subject} - {failedCourse?.number} {failedCourse?.section}</strong> conflicts with an existing course you have added.
          </p>

          <p>
            Here are some alternative courses:
          </p>

          <ul style={{ paddingLeft: "20px" }}>
            {alternatives.map(altCourse => (
              <li key={altCourse.subject + altCourse.number + altCourse.section + altCourse.semester} style={{ marginBottom: "10px" }}>
                <div style={{ fontWeight: "bold" }}>
                  {altCourse.subject} - {altCourse.number}{altCourse.section}
                </div>
                <div>
                  {altCourse.name}
                </div>
                <div style={{ color: "#555", fontSize: "14px" }}>
                  Professor: {altCourse.faculty.join(", ")} | Credits: {altCourse.credits}
                </div>
                <button
                  onClick={() => addAlternativeCourse(altCourse)}
                  style={{
                    marginTop: "5px",
                    padding: "6px 12px",
                    background: "#007bff",
                    color: "white",
                    border: "none",
                    borderRadius: "4px",
                    cursor: "pointer"
                  }}
                >
                  Add as Alternative
                </button>
              </li>
            ))}
          </ul>

        </div>

      </div>
    )}

{showLogin && (
  <div
    style={{
      position: "fixed",
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      background: "rgba(0,0,0,0.6)",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      zIndex: 2000
    }}
  >
    <div
      style={{
        background: "white",
        padding: "30px",
        borderRadius: "10px",
        width: "300px",
        textAlign: "center"
      }}
    >
      <h2>Login</h2>

      <input
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        style={{ width: "100%", marginBottom: "10px", padding: "8px" }}
      />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        style={{ width: "100%", marginBottom: "15px", padding: "8px" }}
      />

      {loginError && (
        <div style={{ color: "red", marginBottom: "10px", fontSize: "14px" }}>
          {loginError}
        </div>
      )}

  {signupError && (
    <div style={{ color: "red", marginBottom: "10px", fontSize: "14px" }}>
      {signupError}
    </div>
  )}

  {signupSuccess && (
    <div style={{ color: "green", marginBottom: "10px", fontSize: "14px" }}>
      {signupSuccess}
    </div>
  )}

      <button
        onClick={handleLogin}
        style={{ width: "100%", marginBottom: "10px" }}
      >
        Login
      </button>

      <button
        onClick={handleSignup}
        style={{ width: "100%", marginBottom: "10px" }}
      >
        Sign Up
      </button>

      <button
        onClick={() => {
          setShowLogin(false);
          setLoginError("");
          setSignupError("");
          setSignupSuccess("");
        }}
        style={{ width: "100%" }}
      >
        Cancel
      </button>
    </div>
  </div>
)}

    </div>
  );
}

