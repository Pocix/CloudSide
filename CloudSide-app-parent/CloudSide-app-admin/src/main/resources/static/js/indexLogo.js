//自定义js

//公共配置


$(document).ready(function () {
    var container = document.getElementById('LogoContainer');
    var renderer = new FSS.SVGRenderer();
    var scene = new FSS.Scene();
    var light = new FSS.Light('#39b4ef', '#FFFFFF');//#356799
    var geometry = new FSS.Plane(220, 178, 6, 4);
    var material = new FSS.Material('#2E3F50', '#2E3F50');//2E3F50
    var mesh = new FSS.Mesh(geometry, material);
    var now, start = Date.now();

    function initialise() {
      scene.add(mesh);
      scene.add(light);
      container.appendChild(renderer.element);
    }

    function resize() {
      renderer.setSize(container.offsetWidth, container.offsetHeight);
    }

    function animate() {
      now = Date.now() - start;
      light.setPosition(300*Math.sin(now*0.001), 200*Math.cos(now*0.0005), 60);
      renderer.render(scene);
      requestAnimationFrame(animate);
    }

    initialise();
    resize();
    animate();

});

