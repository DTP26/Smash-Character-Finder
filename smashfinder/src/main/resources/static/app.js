document.getElementById('preferencesForm').addEventListener('submit', async (e) => {
  e.preventDefault();

  const formGroups = document.querySelectorAll('.form-group');
  const requirements = {};

  formGroups.forEach(group => {
    let statValue, statName;
    const select = group.querySelector('select');
    const checkbox = group.querySelector('input[type="checkbox"]');

    if (select) {
      statValue = parseInt(select.value);
      statName = select.name;
    } else if (checkbox) {
      statValue = checkbox.checked ? 1 : 0;
      statName = checkbox.name;
    } else {
      // No recognizable input found; skip this group
      console.log("Value isn't a checkbox or dropdown selection somehow")
      return;
    }
    const checkedRadio = group.querySelector('input[type="radio"]:checked');
    const statImportance = checkedRadio ? parseInt(checkedRadio.value) : -1;
    requirements[statName] = [statValue, statImportance];
  });

  console.log("Submit event fired");


  const response = await fetch('/api/match', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(requirements)
  });

  const fighters = await response.json();
  localStorage.setItem('results', JSON.stringify(fighters));
  window.location.href = 'results.html';
});
