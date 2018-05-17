; Relocating Loader for Stella/Atari	 V2.00	 1993  Tony Tebby

        section loader

        xref    init_hw		 ; initialise hardware
        xref    init_ram		 ; initialise ram
        xref    end_all		 ; end of this segment

        include 'dev8_keys_stella_bl'
memvalid equ    $420
resvalid equ    $426

;+++
; Stella relocating loader for the ST
;---

        clr.l   memvalid
        clr.l   resvalid		 ; invalidate TOS

        move.w  #$2700,sr		 ; no interrupts

        lea     ld_iram(pc),a6	 ; set return address
        bra.l   init_hw		 ; initalise hardware
ld_iram
        lea     ld_srtop(pc),a6	 ; set return address
        bra.l   init_ram		 ; initialise RAM
ld_srtop
        swap    d7			 ; segs * 65536
        lsl.l   #3,d7		 ; half meg segments
        move.l  d7,a7		 ; true memory top

        sub.l   #$8000,a7		 ; allow for test screen

        lea     end_all,a0		 ; base of OS
        move.l  a0,a1		 ; saved

look_end
        move.l  (a0),d0		 ; length
        lea     sbl.hdlen(a0,d0.l),a0
        bne.s   look_end		 ; end

        subq.l  #sbl.hdlen-4,a0	 ; keep zero at end

move_loop
        move.l  -(a0),-(sp)
        cmp.l   a1,a0
        bgt.s   move_loop		 ; copy all
        beq.s   start

        addq.l  #2,sp		 ; overshot by a word

start
        lea     sbl.hdlen+sbl_entry(sp),a0 ; entry relative address
        add.l   (a0),a0
        jmp     (a0)

        end
