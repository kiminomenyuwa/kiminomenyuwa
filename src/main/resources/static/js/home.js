// src/main/resources/static/js/home.js

// Hamburger Menu Toggle
document.getElementById('hamburger').addEventListener('click', function() {
  document.getElementById('hbgMenu').classList.add('open');
  document.getElementById('overlay').classList.add('active');
});

// Overlay Click to Close Menu
document.getElementById('overlay').addEventListener('click', function() {
  document.getElementById('hbgMenu').classList.remove('open');
  document.getElementById('overlay').classList.remove('active');
});

// Profile Menu Toggle
document.getElementById('profile-menu-btn').addEventListener('click', function(event) {
  event.stopPropagation(); // Prevent event from bubbling up
  var dropdown = document.getElementById('dropdown-menu');
  if (dropdown.style.display === 'block') {
    dropdown.style.display = 'none';
  } else {
    dropdown.style.display = 'block';
  }
});

// Close dropdown when clicking outside
document.addEventListener('click', function() {
  var dropdown = document.getElementById('dropdown-menu');
  if (dropdown.style.display === 'block') {
    dropdown.style.display = 'none';
  }
});
