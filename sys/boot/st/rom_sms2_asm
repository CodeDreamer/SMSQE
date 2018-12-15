; ATARI:  SMS2 ROM header and bootloader

        section base

rom_header
        dc.l    $abcdEF42                ; magic
        dc.l    0                        ; only entry
        dc.l    $02fa0000+rom_hend+8-rom_header ; init address
        dc.l    $00fa0000+rom_prog-rom_header   ; start address
        dc.l    0                        ; date and time - no meaning
        dc.l    0                        ; BSS size - no meaning
        dc.w    'SMS2.TOS',0             ; OS name

rom_prog
        pea     rom_hend+8               ; the boot code
        move.w  #$26,-(sp)
        trap    #14

        clr.w   -(sp)                    ; pterm0
        trap    #1                       ; terminate

rom_hend
        end
