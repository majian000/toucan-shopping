const items = document.querySelectorAll(".accordion button");

function toggleAccordion() {
  const itemToggle = this.getAttribute('aria-expanded');
  
  for (i = 0; i < items.length; i++) {
    items[i].setAttribute('aria-expanded', 'false');
    items[i].querySelector(".icon").innerHTML="展开";
  }
  
  if (itemToggle == 'false') {
    this.setAttribute('aria-expanded', 'true');
    this.querySelector(".icon").innerHTML ="收起";
  }
}

items.forEach(item => item.addEventListener('click', toggleAccordion));