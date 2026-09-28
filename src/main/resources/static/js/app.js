function openReview(btn) {
  const id = btn.getAttribute('data-id');
  document.getElementById('reviewAlertId').value = id;
  document.getElementById('reviewAlertDisplay').value = id;
  const panel = document.getElementById('reviewPanel');
  panel.style.display = 'block';
  panel.scrollIntoView({ behavior: 'smooth', block: 'center' });
}
