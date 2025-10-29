document.addEventListener("DOMContentLoaded", () => {
  console.log("✅ JS loaded and DOM ready");

  const formGroups = document.querySelectorAll('.form-group');

  // Attach GIF update listeners
  formGroups.forEach(group => {
    const select = group.querySelector('select');
    const checkboxes = group.querySelectorAll('input[type="checkbox"]');
    const video = group.querySelector('.stat-video');

    if (!video) return;

    // special case with effective range (multiple checkboxes)
    if (checkboxes.length > 1) {
      console.log("effective range case!")
      checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
          const close = checkboxes[0].checked;
          const mid = checkboxes[1].checked;
          const long = checkboxes[2].checked;

          const key = `C${+close}_M${+mid}_L${+long}`;
          video.src = `/statImages/${"EffectiveRange"}_${key}.mp4`;
          console.log("Updated select image:", video.src);
          video.load();
        });
      });
    }
    else if (select) {
      select.addEventListener('change', () => {
        const value = parseInt(select.value);
        video.src = `/statImages/${select.name.replace(/\s+/g, '')}_${value}.mp4`;
        video.load();
        //console.log("Updated select image:", video.src);
      });
    } else if (checkboxes.length == 1) {
      let checkbox = checkboxes[0];
      checkbox.addEventListener('change', () => {
        const value = checkbox.checked ? 1 : 0;
        video.src = checkbox.checked
          ? `/statImages/${checkbox.name.replace(/\s+/g, '')}_checked.mp4`
          : `/statImages/${checkbox.name.replace(/\s+/g, '')}_unchecked.mp4`;
        video.load();
        //console.log("Updated checkbox image:", video.src);
      });
    }
  });
//const statName = select ? select.name : (checkbox ? checkbox.name : null);
  // Handle form submission separately
  const form = document.getElementById('preferencesForm');
  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const requirements = {};

    formGroups.forEach(group => {
      const select = group.querySelector('select');
      const checkboxes = group.querySelectorAll('input[type="checkbox"]');
      const checkedRadio = group.querySelector('input[type="radio"]:checked');
      const statImportance = checkedRadio ? parseInt(checkedRadio.value) : -1;
      // special case for effective range 
      if (checkboxes.length > 1) {
        const close = checkboxes[0].checked;
        const mid = checkboxes[1].checked;
        const long = checkboxes[2].checked;
        
        requirements["Close Range"] = [close ? 1 : 0, statImportance];
        requirements["Mid Range"] = [mid ? 1 : 0, statImportance];
        requirements["Long Range"] = [long ? 1 : 0, statImportance]
      } else if (checkboxes.length == 1) {
        const statName = checkboxes[0].name;
        requirements[statName] = [checkboxes[0].checked ? 1 : 0, statImportance];
      } else if (select) {
        const statName = select.name;
        requirements[statName] = [parseInt(select.value), statImportance]
      }
      
    });

    console.log("Requirements on submit:", requirements);

    const response = await fetch('/api/match', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(requirements)
    });

    const fighters = await response.json();
    localStorage.setItem('results', JSON.stringify(fighters));
    window.location.href = 'results.html';
  });
});
