; Initialise Atari ST hardware

        section init

        xdef    init_hw

        include 'dev8_keys_atari'

;+++
; Atari ST initialisation, clears all interrupt control registers to remove
; all interrupt sources.
;
;       a6 c  p return address
;---
init_hw
        lea     mfp_init,a0              ; set up mfp registers
        lea     at_mfp,a1
        moveq   #mfp_iend-mfp_init-1,d0
ini_mfp
        move.b  (a0)+,(a1)
        addq.l  #2,a1
        dbra    d0,ini_mfp

        move.b  #gen.ctls,gen_ctls
        move.b  #gen.init,gen_ctlw       ; reset control port

        moveq   #0,d0
        jmp     (a6)

mfp_init
        dc.b    0                ; no output data
        dc.b    mfp.acte         ; active edge
        dc.b    mfp.ddir         ; data direction
        dc.b    1<<mfp..tai      ; ADMA timer
        dc.b    1<<mfp..hdi+1<<mfp..tci ; disk int enabled + 200 Hz timer
        dc.b    0,1<<mfp..tci
        dc.b    0,1<<mfp..tci    ; but only 200Hz enabled
        dc.b    0,1<<mfp..tci
        dc.b    mfp.vect ; V140 software end of interrupt (should be automatic)
        dc.b    0,0,$51  ; no timers AB, C timer 200 and serial port D 9600
        dc.b    0,0,192,2 ; no timers AB, C timer 200 and serial port D 9600

        dc.b    0,mfp.npty+mfp.lstp+mfp.8bit+mfp.adiv; set usart
                                 ; no parity, 1.5 stop bits, 8 bit async
        dc.b    mfp.rxen         ; receiver enabled
        dc.b    mfp.txen         ; transmitter enabled

mfp_iend
        ds.w    0

        end
