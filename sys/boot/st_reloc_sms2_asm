; Relocating Loader for SMS2/Atari     V2.00     1986  Tony Tebby   QJUMP

        section loader

        xref    init_hw                  ; initialise hardware
        xref    init_ram                 ; initialise ram
        xref    end_all                  ; end of this segment

        include 'dev8_keys_smsv'         ; load from smsv_end
sys_bot equ     smsv_end
sys_top equ     $20000                   ; only load as far as here
loader  equ     $20000                   ; so we will put the loader here
memvalid equ    $420
resvalid equ    $426

        move.w  #$2700,sr                ; no interrupts

        clr.l   memvalid
        clr.l   resvalid                 ; invalidate TOS

        lea     ld_iram(pc),a6           ; set return address
        bra.l   init_hw                  ; initalise hardware
ld_iram
        lea     ld_srtop(pc),a6          ; set return address
        bra.l   init_ram                 ; initialise RAM
ld_srtop
        swap    d7                       ; segs * 65536
        lsl.l   #3,d7                    ; half meg segments
        move.l  d7,a5                    ; true memory top

        lea     ld_loader,a0             ; load loader
        moveq   #(ld_end-ld_loader)/2-1,d0
        lea     loader,a1

ld_copy
        move.w  (a0)+,(a1)+              ; copy loader
        dbra    d0,ld_copy

        lea     end_all,a0               ; next segment
        move.l  (a0)+,d1                 ; its length
        addq.l  #4,a0                    ; skip checksum
        lea     sys_bot,a1               ; and where it goes

        lea     sys_top-$100,a3          ; allow 64 extensions in table
        lea     -4(a3),a6                ; ignore the first one it is the OS!!

        move.l  a5,a4                    ; no resident procs yet

        jmp     loader                   ; jump to loader

ld_loader
        lea     (a1,d1.l),a2             ; will it go in bottom part of memory?
        cmp.l   a3,a2
        blo.s   ldl_low                  ; ... yes

        sub.l   d1,a4                    ; resident proc
        move.l  a4,a2

        move.l  a2,(a6)+                 ; save address
ldl_hcopy
        move.w  (a0)+,(a2)+              ; copy into high memory
        subq.l  #2,d1
        bhi.s   ldl_hcopy

        bra.s   ldl_next

ldl_low
        move.l  a1,(a6)+                 ; save address
ldl_lcopy
        move.w  (a0)+,(a1)+              ; copy into low memory
        cmp.l   a2,a1                    ; finished?
        blo.s   ldl_lcopy

ldl_next
        move.l  (a0)+,d1                 ; length of next segment
        addq.l  #4,a0                    ; skip checksum
        bne.s   ld_loader


        clr.l   (a6)+                    ; end of address table

        move.l  d6,d0                    ; keystroke

        jmp     sys_bot                  ; go to OS
ld_end

        end
