<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Lì Xì Tết 2026</title>

        <!-- Bootstrap trước -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- CSS của bạn sau -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lixitet.css?v=1">


    </head>

    <body>

        <section class="tet-banner"></section>

        <div class="marquee">
            <div class="marquee-track">
                <img src="${pageContext.request.contextPath}/images/IMAGE2.png">
                <img src="${pageContext.request.contextPath}/images/IMAGE2.png">
            </div>
        </div>




        <!-- Section sóng -->
        <section class="tet-wave-section">

            <div class="floating-bar">

                <img class="bar-base"
                     src="${pageContext.request.contextPath}/images/IMAGE3.png"
                     alt="">

                <img class="bar-overlay"
                     src="${pageContext.request.contextPath}/images/202-83-20260204085553-iymhv.png"
                     alt="">

                <img class="horse"
                     src="${pageContext.request.contextPath}/images/horse.png"
                     alt="">

            </div>

            <div class="deal-grid">

                <div class="deal-card" onclick="bounce(this)">
                    <img src="${pageContext.request.contextPath}/images/IMAGE5.png">
                </div>

                <div class="deal-card big" onclick="bounce(this)">
                    <img src="${pageContext.request.contextPath}/images/IMAGE4.png">
                </div>

                <div class="deal-card" onclick="bounce(this); scrollToImage9();">
                    <img src="${pageContext.request.contextPath}/images/IMAGE7.png">
                </div>

                <div class="deal-card" onclick="bounce(this)" >
                    <img src="${pageContext.request.contextPath}/images/IMAGE6.png">
                </div>

                <div class="deal-card" onclick="bounce(this); scrollToImage9();">
                    <img src="${pageContext.request.contextPath}/images/IMAGE8.png">
                </div>

            </div>

            <div class="xongdat-banner" id="image9">
                <img src="${pageContext.request.contextPath}/images/IMAGE9.png">
            </div>    
        </section>


        <script>
            document.addEventListener("DOMContentLoaded", function () {

                const section = document.querySelector(".tet-wave-section");

                function updateSpotlight() {
                    const rect = section.getBoundingClientRect();
                    const viewportCenter = window.innerHeight / 2;

                    if (rect.top < window.innerHeight && rect.bottom > 0) {
                        const spotlightY = viewportCenter - rect.top;
                        section.style.setProperty('--spotlight-y', spotlightY + 'px');
                    }
                }

                window.addEventListener("scroll", updateSpotlight);
                updateSpotlight();
            });
        </script>

        <script>
            function bounce(el) {
                el.classList.add("bounce");

                setTimeout(function () {
                    el.classList.remove("bounce");
                }, 400);
            }
        </script>

        <script>
            function scrollToImage9() {
                document.getElementById("image9").scrollIntoView({
                    behavior: "smooth"
                });
            }
        </script>

        <script src="https://cdn.jsdelivr.net/npm/fireworks-js@2/dist/index.umd.js"></script>

        <div id="fireworks"></div>


        <script>
            const container = document.getElementById("fireworks");

            const fireworks = new Fireworks.default(container, {
                rocketsPoint: {min: 20, max: 80},
                delay: {min: 40, max: 80}, // bắn chậm hơn
                speed: 1.5,
                acceleration: 1.02,
                friction: 0.95,
                gravity: 1.5,

                particles: 15, // trước là 50 -> giảm xuống
                trace: 2,
                explosion: 3
            });

            fireworks.start();
        </script>
    </body>

